package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Request extends BaseModel {

    @ManyToOne(optional = false)
    Title title;

    @ManyToOne(optional = false)
    User user;


}
