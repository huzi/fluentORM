package at.lemme.orm.fluent;

import at.lemme.orm.fluent.api.*;
import at.lemme.orm.fluent.impl.InsertImpl;
import at.lemme.orm.fluent.impl.SelectImpl;

import java.sql.Connection;
import java.util.Arrays;

/**
 * Created by thomas on 22.01.17.
 */
public class F implements Fluent {

    final Connection connection;

    public F(final Connection connection) {

        this.connection = connection;
    }

    @Override
    public <T> Insert<T> insert(T... objects) {
        return new InsertImpl<>(connection, Arrays.asList(objects));
    }

    @Override
    public <T> Select<T> select(Class<?> T) {
        return new SelectImpl<T>(connection, T);
    }

    @Override
    public <T> Update<T> update(T... objects) {
        return null;
    }

    @Override
    public <T> Delete<T> delete(Class<?> T) {
        return null;
    }

    @Override
    public <T> DeleteObject<T> deleteObject(T object) {
        return null;
    }

    @Override
    public <T> DeleteObjects<T> deleteObjects(T... objects) {
        return null;
    }
}
