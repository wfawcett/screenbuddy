package models;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;



@Entity
public class Redbox extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public Long titleId;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date lastSeen;

    public Boolean soon;

    public String title;
    public Date sortYear;

    public static final Finder<Long, Redbox>  find = new Finder<>(Redbox.class);
}
