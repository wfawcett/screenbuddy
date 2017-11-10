package models;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import org.apache.commons.codec.digest.DigestUtils;



@Entity
public class User extends Model {
    @Id
    public Long id;

    public String email;
    public String hashPass;
    public String name;

    @OneToMany(mappedBy = "user")
    List<Request> requests;

    public static final Finder<Long, User>  find = new Finder<>(User.class);

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
