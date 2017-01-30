package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Condition;
import at.lemme.orm.fluent.api.Delete;
import at.lemme.orm.fluent.api.DeleteObject;
import at.lemme.orm.fluent.api.QueryParameters;
import at.lemme.orm.fluent.impl.metadata.Metadata;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 22.01.17.
 */
public class DeleteImpl<T> implements Delete<T>, DeleteObject<T> {

    private class Parameters implements QueryParameters {
        private List<Object> params = new ArrayList<>();

        @Override
        public void add(Object o) {
            params.add(o);
        }

        @Override
        public void apply(PreparedStatement stmt) {
            int index = 1;
            System.out.println(params);
            try {
                for (Object value : params) {
                    if (value instanceof String) {
                        stmt.setString(index, (String) value);
                    } else if (value instanceof LocalDate) {
                        stmt.setDate(index, Date.valueOf((LocalDate) value));
                    } else if (value instanceof LocalDateTime) {
                        stmt.setTimestamp(index, Timestamp.valueOf((LocalDateTime) value));
                    } else if (value instanceof Integer) {
                        stmt.setInt(index, (int) value);
                    } else {
                        throw new RuntimeException("Type not Supported!");
                    }
                    index++;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

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
                .append(metadata.getTableName())
                .append(" WHERE ").append(condition.toSql(parameters));

        try {
            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            parameters.apply(stmt);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
