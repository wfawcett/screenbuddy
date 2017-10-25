package models;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import java.util.Date;


@Entity
public class User extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String hashPass;

    @Constraints.Required
    public String lastName;

    @Constraints.Required
    public String firstName;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date createTime;
}
