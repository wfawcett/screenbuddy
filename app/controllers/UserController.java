package controllers;

import com.google.inject.Inject;

import models.User;
import org.apache.commons.codec.digest.DigestUtils;
import play.libs.concurrent.HttpExecutionContext;
import play.data.*;
import play.mvc.*;


import javax.annotation.processing.Completion;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.redirect;

public class UserController {

    @Inject
    private FormFactory formFactory;

    @Inject
    private HttpExecutionContext ec;

    public CompletionStage<Result> registerUser(){
        return CompletableFuture.supplyAsync(() -> addUser(), ec.current())
                .thenApply(flag -> redirect(routes.HomeController.index()));
    }

    public boolean addUser(){
        Form<User> form = formFactory.form(User.class);
        User usr = form.bindFromRequest().get();
        usr.hashPass = DigestUtils.sha1Hex(usr.hashPass); // hash the password before inserting it.
        usr.save();
        return true;
    }

    public CompletionStage<Result> getUser(){
        Form<User> form = formFactory.form(User.class);
        User usr = form.bindFromRequest().get();
        String email = usr.email;
        String hashPass = DigestUtils.sha1Hex(usr.hashPass);
        User aUsr = new QUser()

    }


}
