package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.*;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import views.html.account;

import java.util.*;

import static play.mvc.Controller.*;
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

        List<User> requestList = User.find.query()
                .fetch("requests.title", "*")
                .where().eq("id",usr.id)
                .findList();

        List<ObjectNode> subscribedServices = new ArrayList<ObjectNode>();
        List<Service> serviceList = Service.find.all();

        for(Service service : serviceList){
            ObjectNode subscriptionInfo = Json.newObject();
            // see if the subscription is relevant to the user.
            int subCount = UserService.find.query().where()
                    .eq("user_id", usr.id).eq("service_id", service.id).findCount();
            subscriptionInfo.put("subscribed", (subCount == 1)? "" : "notSubscribed");
            subscriptionInfo.put("logo", service.logo);
            subscriptionInfo.put("id", service.id);
            subscriptionInfo.put("name", service.name);
            subscribedServices.add(subscriptionInfo);
        }




//        try{
//            JsonContext json = Ebean.json();
//            String jsonOutput = json.toJson(userList);
//            Logger.debug("########" + jsonOutput);
//        }catch(Exception ex){
//            Logger.debug("@@@@@@@@@@@@badthings");
//        }

        return ok(account.render(requestList,usr,subscribedServices));
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

    public Result changePass(){
        try{
            Map<String,String[]> formData = request().body().asFormUrlEncoded();
            String userId = (formData.get("userId")[0]);
            String newPass = (formData.get("newPass")[0]);
            User user = User.find.byId(Long.valueOf(userId));
            user.updatePassword(newPass);
            return ok();
        }catch (Exception ex){
            return badRequest (ex.getMessage());
        }
    }

    public Result changeRequest(){
        Map<String,String[]> formData = request().body().asFormUrlEncoded(); Logger.debug("formData: ", formData.toString());

        String titleId = (formData.get("titleId")[0]); Logger.debug("titleId: ", titleId);
        String userId = (formData.get("userId")[0]); Logger.debug("userID: ", userId);
        String action = formData.get("action")[0]; Logger.debug("action: ", action);
        Boolean requested = false;
        if(action.equals("add")){
            Request request = new Request();
            request.user = User.find.byId(Long.valueOf(userId));
            request.title = Title.find.byId(Long.valueOf(titleId));
            request.insert();
            requested = true;
        }else{
            Request request = Request.find.query().where()
                    .eq("user_id", userId)
                    .eq("title_id", titleId)
                    .findOne();
            request.delete();
            requested = false;
        }

        return ok(views.html.partials._movieRequestComponent.render(userId,requested));

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
