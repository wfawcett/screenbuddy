package models;

import io.ebean.Finder;
import io.ebean.Model;
import play.Logger;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Request extends Model {
    @Id
    public Long id;

    @ManyToOne(optional = false)
    public Title title;

    @ManyToOne(optional = false)
    public User user;

    public static final Finder<Long, Request> find = new Finder<>(Request.class);

    public static void crawl(){
        Logger.debug("####################### Starting Request Crawl");
    }


}
