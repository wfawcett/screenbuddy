package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import io.ebean.Finder;
import io.ebean.Model;

import java.util.List;


@Entity
public class Service extends Model {
    @Id
    public Long id;
    public String name;
    public String logo;
    @OneToMany(mappedBy = "service" )
    public List<UserService> userServices;



    public static final Finder<Long, Service> find = new Finder<>(Service.class);

}
