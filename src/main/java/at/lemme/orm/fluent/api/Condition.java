package at.lemme.orm.fluent.api;

/**
 * Created by thomas on 22.01.17.
 */
public interface Condition {

    String toSql(QueryParameters parameters);

    static Condition empty() {
        return p -> "( 1 = 1 )";
    }
}
