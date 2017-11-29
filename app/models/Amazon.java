package models;

import controllers.AmazonSearchController;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Amazon extends Model {
    @Id
    public Long id;

    @ManyToOne(optional = false)
    public Title title;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date lastChecked;

    public Boolean available;

    @ManyToOne(optional = false)
    public Service service;

    public String url;

    @PrePersist
    public void getServiceId(){
        Service service = Service.find.query().where().eq("name", "Amazon").findOne();
        this.service = service;
    }

    public static void setup(){
        Service service = Service.find.query().where().eq("name", "Amazon").findOne();
        if(service == null){
            service = new Service();
            service.name = "Amazon";
            service.logo = "amazon.png";
            service.insert();
        }
    }

    public static final Finder<Long, Amazon> find = new Finder<>(Amazon.class);

    public static Boolean isAvailable(Title title){
        Boolean available = false;
        Amazon amazon = Amazon.get(title);
        if(amazon != null && amazon.available == true){
            available = true;

        }
        Logger.debug("isAvailable(" + title.originalTitle + "): " + available);
        return available;
    }

    public static Amazon get(Title title){
        return Amazon.find.query().where().eq("title", title).findOne();
    }

    public static void crawl(){
        // get a list of requested movies we need to check amazon with.
        // titleIds from request that are either missing or available = false
        String sql ="SELECT t.id,  t.tmdb_id,  t.backdrop_path,  t.original_language,  t.overview,   " +
                "t.poster_path,  t.release_date,  t.release_year,  t.status,  t.tagline,  t.original_title, " +
                "t.vote_average,  t.vote_count,  t.popularity  FROM title t " +
                "LEFT OUTER JOIN request r ON r.title_id = t.id " +
                "LEFT OUTER JOIN amazon a ON a.title_id = t.id " +
                "WHERE (a.id IS NULL OR (a.available = FALSE AND a.last_checked = SUBDATE(CURDATE(),1)))" +
                "AND r.id IS NOT NULL " +
                "LIMIT 1";

        RawSql rawSql = RawSqlBuilder.parse(sql)
                .columnMapping("t.id", "id")
                .columnMapping("t.tmdb_id", "tmdbId")
                .columnMapping("t.backdrop_path", "backdropPath")
                .columnMapping("t.original_language", "originalLanguage")
                .columnMapping("t.overview", "overview")
                .columnMapping("t.poster_path", "posterPath")
                .columnMapping("t.release_date", "releaseDate")
                .columnMapping("t.release_year", "releaseYear")
                .columnMapping("t.status", "status")
                .columnMapping("t.tagline", "tagline")
                .columnMapping("t.original_title", "originalTitle")
                .columnMapping("t.vote_average", "voteAverage")
                .columnMapping("t.vote_count", "voteCount")
                .columnMapping("t.popularity", "popularity")
                .create();
        Title nextTitle = Title.find.query().setRawSql(rawSql).findOne();
        if(nextTitle != null){
            AmazonSearchController amazonSearchController = new AmazonSearchController();

            String amazonUrl = amazonSearchController.getAmazonUrl(nextTitle);
            boolean isAvailable = amazonUrl != null;
            Amazon amazon = Amazon.find.query().where().eq("title_id", nextTitle.id).findOne();
            if(amazon == null){
                amazon = new Amazon();
                amazon.title = nextTitle;
                amazon.lastChecked = new Date();
                amazon.available = isAvailable;
                amazon.url = amazonUrl;
                amazon.insert();
            }else{
                amazon.available = isAvailable;
                amazon.url = amazonUrl;
                amazon.update();
            }
        }
    }
}
