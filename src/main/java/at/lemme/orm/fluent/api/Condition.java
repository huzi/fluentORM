package at.lemme.orm.fluent.api;

/**
 * Created by thomas on 22.01.17.
 */
public interface Condition {


    static Condition empty() {
        return new Condition() {
        };
    }
}
