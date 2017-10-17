package models;

import javax.persistence.Entity;

import play.data.format.Formats;
import play.data.validation.Constraints;

import java.util.Date;

@Entity
public class Redbox extends BaseModel {

    @Constraints.Required
    public int titleId;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date lastSeen;
}
