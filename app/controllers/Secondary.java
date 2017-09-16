package controllers;

import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Secondary extends Controller {

    public Result signUp() {
        return ok(views.html.index.render());
    }

    public Result search() {
        return ok(views.html.index.render());
    }

}