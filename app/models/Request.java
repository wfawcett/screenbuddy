package models;

import io.ebean.Finder;
import io.ebean.Model;
import play.Logger;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.persistence.*;
import java.util.*;

@Entity
public class Request extends Model {
    @Id
    public Long id;

    @ManyToOne(optional = false)
    public Title title;

    @ManyToOne(optional = false)
    public User user;

    @OneToMany(cascade = CascadeType.ALL)
    public List<RequestService> requestServiceList;

    // after insert we will create one requestService for each service the user has
    @PostPersist
    public void registerRequestService(){
        Logger.debug("Starting post persist" + this.id);
        List<UserService> userServices = this.user.userServices;
        Logger.debug("userserviceLength: " + userServices.size());
        for(UserService userService : userServices){
            Service service = userService.service;
            RequestService requestService = new RequestService();
            requestService.service = service;
            requestService.request = this;
            requestService.complete = false;
            this.requestServiceList.add(requestService);
        }
    }

    public static HashMap<User,HashMap<Title,List<Service>>> getMovieUpdates(){
        List<RequestService> requestServices = RequestService.find.query().where().eq("complete", false).findList();

        HashMap<User,HashMap<Title,List<Service>>> userTitleMap = new HashMap<User,HashMap<Title,List<Service>>>();
        HashMap<Title,List<Service>> titleServiceMap = new HashMap<Title,List<Service>>();

        for(RequestService requestService : requestServices){
            User user = requestService.request.user;
            Title title = requestService.request.title;

            List<Service> availableServices = new ArrayList<>();
            for(UserService userService : user.userServices){
                if(userService.service.name.equals("Redbox") && Redbox.isAvailable(title)){
                    availableServices.add(userService.service);
                }

                if(userService.service.name.equals("Amazon") && Amazon.isAvailable(title)){
                    availableServices.add(userService.service);
                }
            }
            if(availableServices.size() > 0){
                titleServiceMap.put(title, availableServices); // Title: [Service1, Service2]
                userTitleMap.put(user, titleServiceMap);
                requestService.complete = true;
                requestService.update();
            }
        }
        return userTitleMap;
    }


    public static final Finder<Long, Request> find = new Finder<>(Request.class);

    public static void crawl(MailerClient mailerClient){
        HashMap<User,HashMap<Title,List<Service>>> userTitleMap = getMovieUpdates();
        // now there should be a map of users with their available movies loaded into it.
        for(User user: userTitleMap.keySet()){
            Map<Title,List<Service>> movieInfo = userTitleMap.get(user);
            List<Title> titles = new ArrayList<Title>(movieInfo.keySet());
            String subject;
            if(titles.size() == 1){
                Title title = titles.get(0);
                subject = "Your movie " + title.originalTitle + " is available!";
            }else{
                subject = "You have new movies available";
            }
            Email email = new Email()
                    .setSubject(subject)
                    .addTo(user.email)
                    .setFrom("buddy@screenbuddy.net")
                    .setBodyHtml(views.html.mailers.movieMailer.render(movieInfo, user, subject).body());
            mailerClient.send(email);
        }
    }


}
