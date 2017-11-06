package controllers;

import models.Title;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
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
        return ok(views.html.signup.render() );

    }

    public Result search() {
        return ok(views.html.search.render() );
    }

    public CompletionStage<Result> searchResults(String phrase) {
        String searchUrl = "https://api.themoviedb.org/3/search/movie";
        WSRequest request = ws.url(searchUrl);
        WSRequest complexRequest = request
                .addQueryParameter("api_key", "274472d0b063eec06615cfed6a703b95")
                .addQueryParameter("language", "en-US")
                .addQueryParameter("query", phrase)
                .addQueryParameter("page", "1")
                .addQueryParameter("'include_adult'", "false");
        return complexRequest.get().thenApply(response -> ok(views.html.searchResults.render(
                response.asJson(),
                response.asJson().get("total_results").asInt()
        )));
    }

    public CompletionStage<Result> movie(String movieId) {
        return Title.getByTmdbId(Integer.valueOf(movieId), ws)
                .thenApply(movieData -> ok(views.html.movie.render(movieData)));

    }



}