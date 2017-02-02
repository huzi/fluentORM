package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Condition;
import at.lemme.orm.fluent.api.Delete;
import at.lemme.orm.fluent.api.DeleteObject;
import at.lemme.orm.fluent.api.DeleteObjects;
import at.lemme.orm.fluent.impl.metadata.Metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by thomas on 22.01.17.
 */
public class DeleteImpl<T> implements Delete<T>, DeleteObject<T>, DeleteObjects {

    private final Connection connection;
    private final Class<?> entityClass;
    private final Metadata metadata;

    private Condition condition = Condition.empty();

    public DeleteImpl(Connection connection, Class<?> clazz) {
        this.connection = connection;

        entityClass = clazz;
        metadata = Metadata.of(entityClass);
    }

    public Metadata metadata() {
        return metadata;
    }

    @Override
    public <T> Delete<T> where(Condition condition) {
        this.condition = condition;
        return (Delete<T>) this;
    }

    @Override
    public void execute() {
        Parameters parameters = new Parameters();

        StringBuilder sql = new StringBuilder("DELETE FROM ")
                .append(metadata.tableName())
                .append(" WHERE ").append(condition.toSql(metadata, parameters));

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            parameters.apply(stmt);
            System.out.println(stmt);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
