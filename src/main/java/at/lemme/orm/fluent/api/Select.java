package at.lemme.orm.fluent.api;

/**
 * Created by thomas on 22.01.17.
 */
public interface Select<T> extends Fetchable<T> {

    <T> Select<T> with(String... relation);

    <T> Select<T> where(Condition condition);

    <T> Select<T> orderBy(String attribute, Order order);

    <T> Select<T> limit(int limit);

    <T> Select<T> limit(int limit, int offset);

}
