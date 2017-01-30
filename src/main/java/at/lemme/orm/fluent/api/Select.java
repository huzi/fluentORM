package at.lemme.orm.fluent.api;

/**
 * Created by thomas on 22.01.17.
 */
public interface Select<T> extends Fetchable<T> {

    <T> Select<? extends T> where(Condition condition);

    <T> Select<T> orderBy(String attribute, Order order);

    <T> Select<T> limit(int i);

    <T> Select<T> limit(int start, int i);

}
