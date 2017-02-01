package at.lemme.orm.fluent.api;

import at.lemme.orm.fluent.impl.metadata.Metadata;

/**
 * Created by thomas on 22.01.17.
 */
public interface Condition {

    String toSql(Metadata metadata, QueryParameters parameters);

    static Condition empty() {
        return (metadata, p) -> "( 1 = 1 )";
    }
}
