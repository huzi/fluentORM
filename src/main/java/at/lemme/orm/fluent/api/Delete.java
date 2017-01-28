package at.lemme.orm.fluent.api;

/**
 * Created by thomas on 22.01.17.
 */
public interface Delete<T> extends Executable {

    <T> Delete<T> where(Condition condition);


}
