package models;

import io.ebean.Ebean;
import play.libs.ws.WSRequest;

import javax.inject.Inject;
import javax.persistence.Entity;
import java.util.Date;
import play.libs.ws.*;

@Entity
public class Title extends BaseModel {

    public int tmdbId;
    public String backdropPath;
    public String originalLanguage;
    public String overview;
    public String posterPath;
    public Date releaseDate;
    public String status;
    public String tagline;
    public String title;
    public Double voteAverage;
    public int voteCount;
    public Double popularity;

    private final WSClient ws;

    @Inject
    public Title(WSClient ws){ this.ws = ws; }

    public Title getByTmdbId(int tmdbId ){
        Title movieInfo = null;
        movieInfo = Ebean.find(Title.class)
                .where()
                .eq("tmdbId", tmdbId)
                .findOne();
        if(movieInfo == null){

        }
    }

    public Title buildTmdbEntry(int tmdbId){
        WSRequest request = ws.url("https://api.themoviedb.org/3/movie/" + tmdbId);
        WSRequest complexRequest = request.addQueryParameter("api_key", "274472d0b063eec06615cfed6a703b95");
        complexRequest.get()
    }




}
