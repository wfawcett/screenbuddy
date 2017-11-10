package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

@Entity
public class Service extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String name;

    public static final Finder<Long, Service> find = new Finder<>(Service.class);

}
