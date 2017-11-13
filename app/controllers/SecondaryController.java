package controllers;

import models.Title;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
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

    public Result search() {
        String username = session("username");
        return ok(views.html.search.render(username) );
    }

    public CompletionStage<Result> searchResults(String phrase) {
        String username = session("username");
        return ws.url("https://api.themoviedb.org/3/search/movie")
                .addQueryParameter("api_key", "274472d0b063eec06615cfed6a703b95")
                .addQueryParameter("language", "en-US")
                .addQueryParameter("query", phrase)
                .addQueryParameter("page", "1")
                .addQueryParameter("'include_adult'", "false")
                .get().thenApply(response ->{
                    int resultCount = response.asJson().get("total_results").asInt();
                    resultCount = resultCount > 10 ? 10 : resultCount;
                    return ok(views.html.searchResults.render(response.asJson(),resultCount, username));
                });
    }

    public CompletionStage<Result> movie(String movieId) {
        String username = session("username");
        return Title.getByTmdbId(Integer.valueOf(movieId), ws)
                .thenApply(movieData -> ok(views.html.movie.render(movieData, username)));

    }



}