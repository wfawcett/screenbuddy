package models;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Entity
public class Title extends BaseModel {

    public int tmdbId;
    public String backdropPath;
    public String originalLanguage;

    @Column(columnDefinition = "TEXT")
    public String overview;
    public String posterPath;
    public Date releaseDate;
    public String status;
    public String tagline;
    public String originalTitle;
    public Double voteAverage;
    public int voteCount;
    public Double popularity;

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
            DateFormat format = new SimpleDateFormat("yyyy-MMMM-d", Locale.ENGLISH);
            this.releaseDate = format.parse(api.get("release_date").asText());
        }catch (ParseException pEx){
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
}
