package at.lemme.fluent.orm.metadata.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by thomas on 11.12.16.
 */
@Entity
public class University {

    @Id
    String id;

    String name;

    Integer foundingYear;
}
