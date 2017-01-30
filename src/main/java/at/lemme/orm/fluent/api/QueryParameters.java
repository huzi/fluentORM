package at.lemme.orm.fluent.api;

import java.sql.PreparedStatement;

/**
 * Created by thomas on 28.01.17.
 */
public interface QueryParameters {
    void add(Object o);
    void apply(PreparedStatement stmt);
}
