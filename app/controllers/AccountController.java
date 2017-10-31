package controllers;

import com.google.inject.Inject;
import io.ebean.Ebean;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import static play.mvc.Controller.flash;
import static play.mvc.Controller.session;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

public class AccountController {
    @Inject
    private FormFactory formFactory;

    private User usr;

    public Result index() {
        if (authorized() == false) return bounceThem();
        return ok(views.html.account.render());
    }

    public Result login() {
        String message = flash("authentication");
        return ok(views.html.login.render(message));
    }

    public Result authenticate() {
        Form<User> form = formFactory.form(User.class);
        User userFormObject = form.bindFromRequest().get();
        User authorizedUser = User.authCheck(userFormObject.email, userFormObject.hashPass);
        if (authorizedUser == null) {
            flash("authentication", "Incorrect email or password, please try again");
            return bounceThem();
        } else {
            return redirect("/account/");
        }
    }

    public boolean authorized() {
        String userId = session("userid");
        usr = null;
        if (userId != null) {
            usr = Ebean.find(User.class, userId);
        }
        return usr != null;
    }

    public Result bounceThem() {
        return redirect("/login");
    }

}
