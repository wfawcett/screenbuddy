package models;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Finder;
import io.ebean.Model;
import play.Logger;
import play.data.format.Formats;
import play.libs.Json;
import play.libs.ws.WSClient;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
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
    public int sortYear;

    public static final Finder<Long, Redbox>  find = new Finder<>(Redbox.class);

    public static Boolean isAvailable(Title title){
        Boolean available = false;
        Redbox redbox = Redbox.find.query().where().eq("title", title).findOne();
        if(redbox != null && redbox.soon == false){
            available = true;
        }
        return available;
    }

    public static void titleLinker(){
        // get all the title_id null records:
        List<Redbox> redboxes = Redbox.find.query().where().eq("title_id", null).findList();
        for(Redbox redbox : redboxes){
            Title title = Title.find.query().where().eq("original_title", redbox.titleName).eq("release_year", redbox.sortYear).findOne();
            if(title != null){
                redbox.title = title;
                redbox.save();
            }
        }


    }

    public static void crawl(WSClient ws){
        Logger.debug("####################### Starting Redbox Crawl");
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
                            String resultTitle = title.get("name").asText();
                            int resultSortYear = Integer.valueOf(title.get("sortDate").asText().substring(0,4));

                            Redbox redbox = Redbox.find.query().where()
                                    .eq("titleName", resultTitle)
                                    .eq("sortYear", resultSortYear)
                                    .findOne();
                            if(redbox == null){redbox = new Redbox();} // if result was empty, start a new one.
                            redbox.lastSeen = new Date();
                            redbox.soon = title.get("soon").asBoolean();
                            redbox.titleName = resultTitle;
                            redbox.sortYear = resultSortYear;
                            redbox.save();
                        }
                    }
                    return response;
                });
    }
}
