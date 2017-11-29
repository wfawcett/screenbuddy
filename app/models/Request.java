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
        List<UserService> userServices = this.user.userServices;
        for(UserService userService : userServices){
            Service service = userService.service;
            RequestService requestService = new RequestService();
            requestService.service = service;
            requestService.request = this;
            requestService.complete = false;
            this.requestServiceList.add(requestService);
        }
    }

    public static HashMap<User,HashMap<Title,List<RequestService>>> getMovieUpdates(){
        List<RequestService> requestServices = RequestService.find.query().where().eq("complete", false).findList();

        HashMap<User,HashMap<Title,List<RequestService>>> userTitleMap = new HashMap<User,HashMap<Title,List<RequestService>>>();
        HashMap<Title,List<RequestService>> titleServiceMap = new HashMap<Title,List<RequestService>>();
        List<RequestService> availableServices = new ArrayList<>();
        for(RequestService requestService : requestServices){
            User user = requestService.request.user;
            Title title = requestService.request.title;

            if(requestService.service.name.equals("Redbox") && Redbox.isAvailable(title)){
                Redbox redbox = Redbox.get(title);
                Logger.debug("adding " +redbox.title.originalTitle + " to redbox availability ");
                requestService.url = redbox.url;
                availableServices.add(requestService);
            }

            if(requestService.service.name.equals("Amazon") && Amazon.isAvailable(title)){
                Amazon amazon = Amazon.get(title);
                Logger.debug("adding " +amazon.title.originalTitle + " to amazon availability ");
                requestService.url = amazon.url;
                availableServices.add(requestService);
            }

            if(availableServices.size() > 0){
                Logger.debug("availableServices size: " + availableServices.size());
                titleServiceMap.put(title, availableServices); // Title: [Service1, Service2]
                userTitleMap.put(user, titleServiceMap);
            }
        }
        return userTitleMap;
    }


    public static final Finder<Long, Request> find = new Finder<>(Request.class);

    public static void crawl(MailerClient mailerClient){
        HashMap<User,HashMap<Title,List<RequestService>>> userTitleMap = getMovieUpdates();
        // now there should be a map of users with their available movies loaded into it.
        for(User user: userTitleMap.keySet()){
            Map<Title,List<RequestService>> movieInfo = userTitleMap.get(user);
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
