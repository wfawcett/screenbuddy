package models;

import io.ebean.Ebean;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
public class User extends BaseModel {

    public String email;
    public String hashPass;
    public String name;

    @OneToMany(mappedBy = "user")
    List<Request> requests;

    public static User authCheck(String email, String password){
        User usr = null;
        if(email != null && password != null){
            String hashPass = DigestUtils.sha1Hex(password);
            usr = Ebean.find(User.class)
                    .where()
                    .eq("email", email )
                    .eq("hashPass", hashPass)
                    .findOne();
        }
        return usr;
    }
}
