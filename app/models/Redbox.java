package models;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.SecondaryController;
import helpers.Levenshtein;
import io.ebean.*;
import play.Logger;
import play.data.format.Formats;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.twirl.api.utils.StringEscapeUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Redbox extends Model {

    @Id
    public Long id;

    @ManyToOne(optional = true)
    public Title title;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date lastSeen;

    public Boolean soon;

    @ManyToOne(optional = false)
    public Service service;

    @PrePersist
    public void getServiceId(){
        Service service = Service.find.query().where().eq("name", "Redbox").findOne();
        this.service = service;
    }

    public static void setup(){
        Service service = Service.find.query().where().eq("name", "Redbox").findOne();
        if(service == null){
            service = new Service();
            service.name = "Redbox";
            service.logo = "redbox.png";
            service.insert();
        }
    }

    public String titleName;

    public static final Finder<Long, Redbox>  find = new Finder<>(Redbox.class);

    public static Boolean isAvailable(Title title){
        Boolean available = false;
        Redbox redbox = Redbox.find.query().where().eq("title", title).findOne();
        if(redbox != null && redbox.soon == false){
            available = true;
        }
        return available;
    }

    public static void titleLinker(WSClient ws, int maxRows){
        Logger.debug("######## running titleLinker ########");
        // get all the title_id null records:
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        Date lookback = cal.getTime();

        List<Redbox> redboxes = Redbox.find.query().where()
                .eq("title_id", null)
                .or()
                    .lt("last_seen", lookback).eq("last_seen", null)
                .endOr()
                .setMaxRows(maxRows)
                .findList();
        for(Redbox redbox : redboxes){
            String cleanName = redbox.titleName.replaceAll("\\([^\\)]*\\)", ""); // strip the (2017) style dates from titles.
            cleanName = StringEscapeUtils.escapeEcmaScript(redbox.titleName); // escape illegal characters.
            Boolean dbMatchFound = false;

            // A bit tricky: use the full text index to find a title that is similar to the redbox title.
            // we then need to score the candidate title against the redbox name and if we are better than 60% we accept the match.
            String candidateSql = "SELECT original_title FROM title WHERE MATCH(original_title) AGAINST(:title IN NATURAL LANGUAGE MODE) limit 1";
            SqlRow candidate = Ebean.createSqlQuery(candidateSql).setParameter("title", cleanName).findOne();
            if(candidate != null){
                String candidateName = candidate.getString("original_title");
                Logger.debug("@@ Scoring: " + candidateName + " for " + cleanName);
                String scoreSql = "SELECT id, original_title, " +
                        "MATCH(original_title) AGAINST(:title IN NATURAL LANGUAGE MODE) / MATCH(original_title) AGAINST(:candidate IN NATURAL LANGUAGE MODE) AS score " +
                        "FROM title " +
                        "WHERE MATCH(original_title) AGAINST(:title IN NATURAL LANGUAGE MODE) " +
                        "HAVING score > 0.6 limit 1";
                SqlRow score = Ebean.createSqlQuery(scoreSql)
                        .setParameter("title", cleanName)
                        .setParameter("candidate", candidateName)
                        .findOne();
                if(score != null){
                    String existingId = score.getString("id");
                    Title title = Title.find.byId(Long.valueOf(existingId));
                    redbox.title = title;
                    redbox.lastSeen = new Date();
                    dbMatchFound = true;
                    redbox.save();
                }
            }

            // The database match failed so we need to go out to TMDB to get the data.
            if(dbMatchFound == false) { // need to go to tmdb to get the info.
                Logger.debug("Getting data from tmdb for: " + redbox.titleName);
                cleanName = redbox.titleName.replaceAll("\\([^\\)]*\\)", "");
                SecondaryController.searchTmdb(ws, cleanName)
                        .thenApply((response) -> {
                            Logger.debug("got response from searchTmdb for: " + redbox.titleName);
                            JsonNode topResult = response.get("results").get(0);
                            Logger.debug("topResult: " + Json.stringify(topResult));
                            if (topResult == null) {
                                redbox.save();
                                return null; // stop processing.
                            }
                            int topResultId = topResult.get("id").asInt();
                            Logger.debug("calling tmdb with id: " + topResultId);
                            return Title.asyncGetFromApiById(topResultId, ws)
                                    .thenApply((newTitle) -> {
                                        Logger.debug("Got titledata from TMDB");
                                        redbox.title = newTitle;
                                        redbox.save();
                                        return newTitle;
                                    });
                        });
            }
        }

    }

    public static void crawl(WSClient ws){
        Logger.debug("######## running redbox crawl ########");
        ws.url("http://www.redbox.com/api/product/js/__titles").get()
                .thenApply((response)->{
                    String body = response.getBody();
                    body = body.replace("var __titles = ", "");
                    JsonNode titleData = Json.parse(body);
                    for(JsonNode title : titleData){
                        Logger.debug("--- got a title: " + title.get("sortName").asText());
                        // productType 1 = movie, 5 = video game
                        // fmt 1 = dvd, 2 = blu-ray (blu-ray sometimes adds blu ray to the title, or sortName, so ignore them);
                        // soon = 1 means it isn't available yet. so ignore it.
                        if(title.get("productType").asText().equals("1") && title.get("fmt").asText().equals("1") && title.get("soon").asText().equals("0")){
                            String resultTitle = title.get("sortName").asText();
                            Redbox redbox = Redbox.find.query().where().eq("titleName", resultTitle).findOne();
                            if(redbox == null){redbox = new Redbox();} // if result was empty, start a new one.
                            redbox.soon = title.get("soon").asBoolean();
                            redbox.titleName = resultTitle;
                            redbox.save();
                        }
                    }
                    return response;
                });
    }
}
