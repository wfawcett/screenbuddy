package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.ebean.Finder;
import io.ebean.Model;
import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;

import java.util.Date;

@Entity
public class Amazon extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public int titleId;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date lastSeen;

    public static final Finder<Long, Amazon> find = new Finder<>(Amazon.class);

    public static void crawl(){
        Logger.debug("####################### Starting Amazon Crawl");
    }
}
