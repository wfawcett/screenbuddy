package models;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;
import play.Logger;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Entity
public class Title extends Model {
    @Id
    public Long id;
    public int tmdbId;
    public String backdropPath;
    public String originalLanguage;

    @Column(columnDefinition = "TEXT")
    public String overview;
    public String posterPath;
    public Date releaseDate;
    public int releaseYear;
    public String status;
    public String tagline;
    public String originalTitle;
    public Double voteAverage;
    public int voteCount;
    public Double popularity;

    public static final Finder<Long, Title> find = new Finder<>(Title.class);

    private final WSClient ws;

    @Inject
    public Title(WSClient ws){
        this.ws = ws;
    }

    public Title(JsonNode api, WSClient ws){
        this.ws = ws;
        this.tmdbId = api.get("id").asInt();
        this.backdropPath= api.get("backdrop_path").asText();
        this.originalLanguage= api.get("original_language").asText();
        this.overview= api.get("overview").asText();
        this.posterPath= api.get("poster_path").asText();
        this.status= api.get("status").asText();
        this.tagline= api.get("tagline").asText();
        this.originalTitle= api.get("title").asText();
        this.voteAverage= api.get("vote_average").asDouble();
        this.voteCount= api.get("vote_count").asInt();
        this.popularity= api.get("popularity").asDouble();

        try{
            DateFormat format = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
            Date parsedDate = format.parse(api.get("release_date").asText());
            String yearString =(new SimpleDateFormat("yyyy")).format(parsedDate);
            this.releaseYear = Integer.parseInt(yearString);
            this.releaseDate = parsedDate;
            Logger.debug("########okay so far");
        }catch (Exception ex){
            Logger.error("ParseException: " + ex.toString());
            this.releaseDate = null;
        }
    }

    public static CompletionStage<Title> asyncGetFromDbById(int tmdbId){
        return CompletableFuture.supplyAsync(()->{
            return Ebean.find(Title.class);
        }).thenApply(finder -> {
            return finder.where().eq("tmdbId", tmdbId).findOne();
        });
    }

    public static CompletionStage<Title> asyncGetFromApiById(int tmdbId, WSClient ws){
        return ws.url("https://api.themoviedb.org/3/movie/" + tmdbId)
                .addQueryParameter("api_key", "274472d0b063eec06615cfed6a703b95")
                .get()
                .thenApply(response -> {
                    Title newTitle = new Title(response.asJson(),ws);
                    newTitle.save();
                    return newTitle;
                });
    }

    public static CompletionStage<Title> getByTmdbId(int tmdbId, WSClient ws ){
        int existing = Ebean.find(Title.class).where().eq("tmdbId", tmdbId).findCount();
        if(existing > 0 ){ // it is in the database;
            return asyncGetFromDbById(tmdbId);
        }else{
            return asyncGetFromApiById(tmdbId, ws);
        }
    }

    // checks with the title table and if we don't have it yet will get it from tmdb then we will save the link the entry in redbox
    public static void importForRedbox(String title, int releaseYear, Date lastSeen, Boolean soon){
        try{
            Title existing = Ebean.find(Title.class).where().eq("originalTitle", title ).eq("releaseYear", releaseYear).findOne();
            if(existing != null){ // we found one record, lets link it and we are done.
                Redbox newRedbox = new Redbox();
                newRedbox.titleId = existing.id;
                newRedbox.lastSeen = Calendar.getInstance().getTime();
                newRedbox.soon = soon;
                newRedbox.save();
            }else{ // we didn't find it so we need to query the api and get the data.
                // query the api, get the data and use it to insert into the database then use that id to build the redbox mode.

            }
        }catch(NonUniqueResultException nurex){ // we found more than one record.
            Logger.error("Can't link this because it looks to link to two items. Title:" + title + " ReleaseYear: " + releaseYear);
        }


    }


}
