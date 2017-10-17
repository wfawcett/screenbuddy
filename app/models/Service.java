package models;

import javax.persistence.Entity;
import play.data.validation.Constraints;

@Entity
public class Service extends BaseModel {

    @Constraints.Required
    public String name;

}
