package models;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.SecondaryController;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
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

    public static void titleLinker(WSClient ws){
        // get all the title_id null records:
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        Date lookback = cal.getTime();

        List<Redbox> redboxes = Redbox.find.query().where()
                .eq("title_id", null)
                .or()
                    .lt("last_seen", lookback).eq("last_seen", null)
                .endOr()
                .setMaxRows(2)
                .findList();
        for(Redbox redbox : redboxes){
            String cleanName = StringEscapeUtils.escapeEcmaScript(redbox.titleName);
            Logger.debug("working on: " + cleanName);
            String sql = "select id, tmdb_id, backdrop_path, original_language, overview, poster_path, release_date, " +
                    "release_year, status, tagline, original_title, vote_average, vote_count, popularity, " +
                    "match(original_title) against('" + cleanName + "' in natural language mode) as score " +
                    "from title " +
                    "where match(original_title) against('" + cleanName + "' in natural language mode)" +
                    "HAVING score > 0.7";
            RawSql rawSql = RawSqlBuilder.parse(sql)
                    .columnMapping("id", "id")
                    .columnMapping("tmdb_id", "tmdbId")
                    .columnMapping("backdrop_path", "backdropPath")
                    .columnMapping("original_language", "originalLanguage")
                    .columnMapping("overview", "overview")
                    .columnMapping("poster_path", "posterPath")
                    .columnMapping("release_date", "releaseDate")
                    .columnMapping("release_year", "releaseYear")
                    .columnMapping("status", "status")
                    .columnMapping("tagline", "tagline")
                    .columnMapping("original_title", "originalTitle")
                    .columnMapping("vote_average", "voteAverage")
                    .columnMapping("vote_count", "voteCount")
                    .columnMapping("popularity", "popularity")
                    .columnMappingIgnore("score")
                    .create();
            Title title = Title.find.query().setRawSql(rawSql).findOne();

            redbox.lastSeen = new Date();

            if(title != null){ // we have a match in the database
                Logger.debug("found in the database");
                redbox.title = title;

                redbox.save();
            }else{ // need to go to tmdb to get the info.
                Logger.debug("Getting data from tmdb for: " + redbox.titleName);
                cleanName = redbox.titleName.replaceAll("\\([^\\)]*\\)", "");
                SecondaryController.searchTmdb(ws, cleanName )
                        .thenApply((response) ->{
                            Logger.debug("got response from searchTmdb for: " + redbox.titleName);
                            JsonNode topResult = response.get("results").get(0); Logger.debug("topResult: " + Json.stringify(topResult));
                            if(topResult == null){
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
                                        return title;
                                    });
                        });
            }



        }


    }

    public static void crawl(WSClient ws){
        ws.url("http://www.redbox.com/api/product/js/__titles").get()
                .thenApply((response)->{
                    String body = response.getBody();
                    body = body.replace("var __titles = ", "");
                    JsonNode titleData = Json.parse(body);
                    for(JsonNode title : titleData){
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
