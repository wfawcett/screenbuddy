package controllers;

import com.google.inject.Inject;
import models.User;
import org.apache.commons.codec.digest.DigestUtils;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static play.mvc.Controller.session;

import static play.mvc.Results.redirect;

public class UserController {

    @Inject
    private FormFactory formFactory;

    @Inject
    private HttpExecutionContext ec;

    public CompletionStage<Result> registerUser(){
        Logger.debug("in registerUser");
        return CompletableFuture.supplyAsync(() -> addUser(), ec.current())
                .thenApply(flag -> redirect(routes.AccountController.index()));
    }

    public boolean addUser(){
        Logger.debug("***************adding user.");
        Form<User> form = formFactory.form(User.class);
        User usr = form.bindFromRequest().get();
        usr.hashPass = DigestUtils.sha1Hex(usr.hashPass); // hash the password before inserting it.
        Logger.debug("***************USER: " + usr.toString());
        usr.save();
        session("userid", usr.id.toString()); // set the user in the session.
        session("username", usr.name);
        return true;
    }
}
