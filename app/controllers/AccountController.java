package controllers;

import com.google.inject.Inject;
import io.ebean.Ebean;
import models.User;
import play.*;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import static play.mvc.Controller.session;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

public class AccountController {
    @Inject
    private FormFactory formFactory;

    private User usr;

    public Result index(){
        if (authorized() == false) bounceThem();
        return ok(views.html.account.render() );

    }

    public Result login(){
        return ok(views.html.login.render() );
    }

    public Result authenticate(){
        Form<User> form = formFactory.form(User.class);
        User userFormObject = form.bindFromRequest().get();
        User authorizedUser = User.authCheck(userFormObject.email, userFormObject.hashPass);
        session("userid", authorizedUser.id.toString());
        return redirect("/account/");
    }

    public boolean authorized(){ // they are a stranger if they are not authenticated.
        String userId = session("userid");
        usr = null;
        if(userId != null){
            usr = Ebean.find(User.class, userId);
        }
        return usr != null;
    }

    public Result bounceThem(){
        return redirect("/login");

    }

}
