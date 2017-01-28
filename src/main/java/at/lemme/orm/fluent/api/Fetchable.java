package at.lemme.orm.fluent.api;

import java.util.List;

/**
 * Created by thomas on 22.01.17.
 */
public interface Fetchable<T> {
    <T> List<T> fetch();
}
