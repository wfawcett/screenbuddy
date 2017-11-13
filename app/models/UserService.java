package models;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

@Entity
public class UserService extends Model {
    @Id
    public Long id;

    @ManyToOne(optional = false)
    public Service service;

    @ManyToOne(optional = false)
    public User user;

    public static final Finder<Long, UserService> find = new Finder<>(UserService.class);

}
