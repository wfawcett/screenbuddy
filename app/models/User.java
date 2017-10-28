package models;

import javax.persistence.Entity;

@Entity
public class User extends BaseModel {

    public String email;
    public String hashPass;
    public String name;
}
