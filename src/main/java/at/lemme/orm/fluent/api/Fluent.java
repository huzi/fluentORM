package at.lemme.orm.fluent.api;


/**
 * Created by thomas on 22.01.17.
 */
public interface Fluent {

    <T> Insert<T> insert(T... objects);

    <T> Select<T> select(Class<?> T);

    <T> Update<T> update(T... objects);

    <T> Delete<T> delete(Class<?> T);

    <T> DeleteObject<T> deleteObject(T object);

    <T> DeleteObjects<T> deleteObjects(T... objects);
}
