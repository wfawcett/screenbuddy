package models;

import io.ebean.Ebean;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;


@Entity
public class User extends BaseModel {

    public String email;
    public String hashPass;
    public String name;

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
