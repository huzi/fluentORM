package at.lemme.fluent.orm;

import java.sql.Connection;

/**
 * Created by thomas on 11.11.16.
 */
public class Save<T> {

    final Connection connection;

    String tableName;
    Class<?> entityClass;

    public Save(final Connection connection) {
        this.connection = connection;
    }

    public SaveByObject byObject(T object) {
        this.entityClass = object.getClass();
        this.tableName = entityClass.getSimpleName();
        return new SaveByObject(this, object);
    }
}
