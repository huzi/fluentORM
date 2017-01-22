package at.lemme.fluent.orm.select;

import java.sql.Connection;

/**
 * Created by thomas on 11.11.16.
 */
public class Select {

    final Connection connection;

    String tableName;
    Class<?> entityClass;

    public Select(final Connection connection) {
        this.connection = connection;
    }

    public <T> SelectFrom<T> from(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.tableName = entityClass.getSimpleName();
        return new SelectFrom<T>(this, entityClass);
    }


}
