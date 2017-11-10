package models;
import javax.persistence.Entity;
import javax.persistence.Id;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

@Entity
public class UserService extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public int userId;

    @Constraints.Required
    public int serviceId;

    public static final Finder<Long, UserService> find = new Finder<>(UserService.class);

}
