package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.Logger;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class SecondaryController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    @Inject
    public SecondaryController(WSClient ws) {
        this.ws = ws;
    }

    public Result signUp() {
        String username = session("username");
        return ok(views.html.signup.render(username) );

    }

    public Result mailer(String userId){
        User user = User.find.query().where().eq("id", userId).findOne();
        HashMap<User,HashMap<Title,List<RequestService>>> movieData = Request.getMovieUpdates();
        HashMap<Title,List<RequestService>> userdata = movieData.get(user);
        if(userdata != null){
            List<Title> titles = new ArrayList<Title>(userdata.keySet());
            String subject;
            if(titles.size() == 1){
                Title title = titles.get(0);
                subject = "Your movie " + title.originalTitle + " is available!";
            }else{
                subject = "You have new movies available";
            }
            return ok(views.html.mailers.movieMailer.render(userdata, user, subject));
        }else{
            return ok();// nothing
        }


    }

    public Result search() {
        String username = session("username");
        return ok(views.html.search.render(username) );
    }

    public CompletionStage<Result> searchResults(String phrase) {
        String username = session("username");
        return searchTmdb(ws, phrase)
                .thenApply(result ->{
                    int resultCount = result.get("total_results").asInt();
                    resultCount = resultCount > 10 ? 10 : resultCount;
                    return ok(views.html.searchResults.render(result,resultCount, username));
                });
    }

    public static CompletionStage<JsonNode> searchTmdb(WSClient ws, String phrase){
        phrase = phrase.replaceAll("\\%20", "+");
        return ws.url("https://api.themoviedb.org/3/search/movie")
                .addQueryParameter("api_key", "274472d0b063eec06615cfed6a703b95")
                .addQueryParameter("language", "en-US")
                .addQueryParameter("query", phrase)
                .addQueryParameter("page", "1")
                .addQueryParameter("'include_adult'", "false")
                .get().thenApply(response -> response.asJson());
    }

    public CompletionStage<Result> movie(String movieId) {
        String username = session("username");
        String userId = session("userid");

        int requestCount =Request.find.query().where()
                .eq("title.tmdbId", movieId)
                .eq("user_id", userId)
                .findCount();

        boolean requested = requestCount == 1;
        return Title.getByTmdbId(Integer.valueOf(movieId), ws)
                .thenApply(movieData -> ok(views.html.movie.render(movieData, username, requested, userId)));

    }



}