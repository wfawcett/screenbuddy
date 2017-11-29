package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.ebean.Finder;
import io.ebean.Model;



@Entity
public class RequestService extends Model {
    @Id
    public long id;

    @ManyToOne(optional = false)
    public Request request;

    @ManyToOne(optional = false)
    public Service service;

    public Boolean complete;

    public String url;

    public static final Finder<Long, RequestService> find = new Finder<>(RequestService.class);

}
