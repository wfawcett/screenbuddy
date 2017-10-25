package models;

import javax.persistence.Entity;



import play.data.validation.Constraints;

@Entity
public class Request extends BaseModel {

    @Constraints.Required
    public int titleId;

    @Constraints.Required
    public int userId;


}
