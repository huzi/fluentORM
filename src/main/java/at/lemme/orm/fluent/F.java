package at.lemme.orm.fluent;

import at.lemme.orm.fluent.api.*;
import at.lemme.orm.fluent.impl.DeleteImpl;
import at.lemme.orm.fluent.impl.InsertImpl;
import at.lemme.orm.fluent.impl.SelectImpl;
import at.lemme.orm.fluent.impl.UpdateImpl;

import java.sql.Connection;
import java.util.Arrays;
import java.util.stream.Stream;

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
        return new UpdateImpl<T>(connection, Arrays.asList(objects));
    }

    @Override
    public <T> Delete<T> delete(Class<?> T) {
        return new DeleteImpl<T>(connection, T);
    }

    @Override
    public <T> DeleteObject<T> deleteObject(T object) {
        DeleteImpl<T> delete = new DeleteImpl<>(connection, object.getClass());
        delete.where(Conditions.equals("id", delete.metadata().getColumn("id").getValue(object)));
        return delete;
    }

    @Override
    public <T> DeleteObjects<T> deleteObjects(T... objects) {
        final DeleteImpl<T> delete = new DeleteImpl<>(connection, objects[0].getClass());
        String[] ids = Stream.of(objects)
                .map(t -> {
                    System.out.println(delete);
                    System.out.println(delete.metadata());
                    System.out.println(delete.metadata().getColumn("id"));
                    return delete.metadata().getColumn("id").getValue(t);
                })
                .toArray(size -> new String[size]);
        delete.where(Conditions.in("id", ids));
        return (DeleteObjects<T>) delete;
    }
}
