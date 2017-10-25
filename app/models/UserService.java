package models;
import javax.persistence.Entity;
import play.data.validation.Constraints;

@Entity
public class UserService extends BaseModel {

    @Constraints.Required
    public int userId;

    @Constraints.Required
    public int serviceId;


}
