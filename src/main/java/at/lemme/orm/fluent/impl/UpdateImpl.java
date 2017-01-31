package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Update;
import at.lemme.orm.fluent.impl.metadata.Metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thomas on 22.01.17.
 */
public class UpdateImpl<T> implements Update<T> {

    private final List<T> objects;

    private final Connection connection;
    private final Class<?> entityClass;
    private final Metadata metadata;

    public UpdateImpl(Connection connection, List<T> objects) {
        this.connection = connection;
        this.objects = objects;
        entityClass = objects.get(0).getClass();
        metadata = Metadata.of(entityClass);
    }

    public Metadata metadata() {
        return metadata;
    }

    @Override
    public void execute() {
        final List<String> allColumnsExceptId = metadata.getColumnNames().stream()
                .filter(column -> !"id".equals(column)).collect(Collectors.toList());
        StringBuilder sql = buildSql(allColumnsExceptId);
        try (final PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            objects.forEach(object -> addBatchValues(stmt, object, allColumnsExceptId));
            stmt.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder buildSql(List<String> allColumnsExceptId) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(metadata.getTableName());
        sql.append(" SET ");
        String setStatements = allColumnsExceptId.stream().map(column -> column + " = ?")
                .collect(Collectors.joining(", "));
        sql.append(setStatements);
        sql.append(" WHERE ").append("id").append(" = ?");
        return sql;
    }

    private void addBatchValues(PreparedStatement stmt, T object, List<String> allColumnsExceptId) {
        try {
            addValuesToPreparedStatement(stmt, object, allColumnsExceptId);
            stmt.addBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addValuesToPreparedStatement(PreparedStatement stmt, T o, List<String> allColumnsExceptId) throws NoSuchFieldException, IllegalAccessException, SQLException {

        for (int i = 1; i <= allColumnsExceptId.size(); i++) {
            String columnName = allColumnsExceptId.get(i - 1);
            metadata.getColumn(columnName).setParameter(stmt, i, o);
        }
        metadata.getColumn("id").setParameter(stmt, allColumnsExceptId.size() + 1, o);
    }
}