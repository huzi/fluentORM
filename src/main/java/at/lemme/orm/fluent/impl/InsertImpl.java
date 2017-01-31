package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Insert;
import at.lemme.orm.fluent.impl.metadata.Metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thomas on 22.01.17.
 */
public class InsertImpl<T> implements Insert<T> {

    private final Connection connection;
    private final List<T> objects;
    private final Class<?> entityClass;
    private final Metadata metadata;

    private final String columnString;
    private final String wildCardString;

    public InsertImpl(Connection connection, List<T> objects) {
        this.connection = connection;
        this.objects = new ArrayList<T>(objects);

        entityClass = objects.get(0).getClass();
        metadata = Metadata.of(entityClass);
        columnString =
                metadata.getColumnNames().stream().collect(Collectors.joining(", "));
        wildCardString =
                metadata.getColumnNames().stream().map(c -> "?").collect(Collectors.joining(", "));
    }

    @Override
    public void execute() {
        final StringBuilder sql = buildSql();
        executeSql(sql);
    }

    private void executeSql(StringBuilder sql) {
        try (final PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            objects.forEach(object -> addBatchValues(stmt, object));
            stmt.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder buildSql() {
        final StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(metadata.getTableName());
        sql.append('(').append(columnString).append(')');
        sql.append(" VALUES ");
        sql.append('(').append(wildCardString).append(')');

        System.out.println(sql);
        return sql;
    }

    private void addBatchValues(PreparedStatement stmt, T object) {
        try {
            addValuesToPreparedStatement(stmt, object);
            stmt.addBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addValuesToPreparedStatement(PreparedStatement stmt, T o) throws NoSuchFieldException, IllegalAccessException, SQLException {

        for (int i = 1; i <= metadata.getColumnNames().size(); i++) {
            String columnName = metadata.getColumnNames().get(i - 1);
            metadata.getColumn(columnName).setParameter(stmt, i, o);
        }

    }
}
