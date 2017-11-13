package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.ebean.Ebean;
import io.ebean.text.json.EJson;
import io.ebean.text.json.JsonContext;
import models.Service;
import models.User;
import models.UserService;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import views.html.account;

import java.util.List;
import java.util.Map;

import static play.mvc.Controller.flash;
import static play.mvc.Controller.request;
import static play.mvc.Controller.session;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

public class AccountController {
    @Inject
    private FormFactory formFactory;

    private User usr;

    public Result index() {
        if (authorized() == false) {
            return bounceThem("Authorized Access, please log in");
        }

        List<User> userList = User.find.query()
                .fetch("requests.title", "*")
                .where().eq("id",usr.id)
                .findList();

        try{
            JsonContext json = Ebean.json();
            String jsonOutput = json.toJson(userList);
            Logger.debug("########" + jsonOutput);
        }catch(Exception ex){
            Logger.debug("@@@@@@@@@@@@badthings");
        }

        return ok(account.render(userList,usr));
    }

    public Result login() {
        String message = flash("authentication");
        String username = session("username");
        return ok(views.html.login.render(message,username));
    }

    public Result logout(){
        session().remove("userid");
        session().remove("username");
        return redirect("/");
    }

    public Result authenticate() {
        Form<User> form = formFactory.form(User.class);
        User userFormObject = form.bindFromRequest().get();
        User authorizedUser = User.authCheck(userFormObject.email, userFormObject.hashPass);
        if (authorizedUser == null) {
            return bounceThem("Incorrect email or password, please try again");
        } else {
            session("userid", authorizedUser.id.toString()); // set the user in the session.
            session("username", authorizedUser.name);
            return redirect("/account");
        }
    }

    public boolean authorized() {
        String userId = session("userid");
        usr = null;
        if (userId != null) {
            usr = User.find.byId(Long.valueOf(userId));
        }
        return usr != null;
    }

    public Result bounceThem(String message) {
        flash("authentication", message);
        return redirect("/login");
    }

    public Result changeService(){
        Map<String,String[]> formData = request().body().asFormUrlEncoded();
        String serviceId = (formData.get("serviceId")[0]);
        String userId = (formData.get("userId")[0]);
        String action = formData.get("action")[0];


        if(action.equals("add")){
            User user = User.find.byId(Long.valueOf(userId));
            Service service = Service.find.byId(Long.valueOf(serviceId));
            UserService us = new UserService();
            us.user = user;
            us.service = service;
            us.save();
        }else{
            UserService us = UserService.find.query().where().eq("user_id", userId).eq("service_id" ,serviceId).findOne();
            us.delete();
        }

        boolean success = true;
        if(success){
            return ok();
        }else{
            return badRequest ("bad things");
        }


    }

}
