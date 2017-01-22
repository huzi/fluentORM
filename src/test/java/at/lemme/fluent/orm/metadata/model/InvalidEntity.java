package at.lemme.fluent.orm.metadata.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by thomas on 04.12.16.
 */
@Entity
public class InvalidEntity {
    @Id
    String id;
    String someColumn;

    // Invalid because of no No-Arg Constructor!
    public InvalidEntity(String someColumn) {
        this.someColumn = someColumn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSomeColumn() {
        return someColumn;
    }

    public void setSomeColumn(String someColumn) {
        this.someColumn = someColumn;
    }
}
