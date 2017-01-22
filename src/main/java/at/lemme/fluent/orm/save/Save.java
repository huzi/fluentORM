package at.lemme.fluent.orm.save;

import java.sql.Connection;

/**
 * Created by thomas on 11.11.16.
 */
public class Save {

    final Connection connection;

    String tableName;
    Class<?> entityClass;

    public Save(final Connection connection) {
        this.connection = connection;
    }

    public <T> SaveByObject<T> byObject(T object) {
        this.entityClass = object.getClass();
        this.tableName = entityClass.getSimpleName();
        return new SaveByObject<T>(this, object);
    }
}
