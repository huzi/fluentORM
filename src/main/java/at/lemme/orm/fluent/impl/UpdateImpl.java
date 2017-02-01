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

    @Override
    public void execute() {
        final List<String> allAttributesExceptId = metadata.attributeNames().stream()
                .filter(attribute -> !metadata.id().name().equals(attribute)).collect(Collectors.toList());
        StringBuilder sql = buildSql(allAttributesExceptId);
        try (final PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            objects.forEach(object -> addBatchValues(stmt, object, allAttributesExceptId));
            stmt.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder buildSql(List<String> allAttributesExceptId) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(metadata.tableName());
        sql.append(" SET ");
        String setStatements = allAttributesExceptId.stream()
                .map(attribute -> metadata.columnForAttribute(attribute) + " = ?")
                .collect(Collectors.joining(", "));
        sql.append(setStatements);
        sql.append(" WHERE ").append(metadata.id().columnName()).append(" = ?");
        return sql;
    }

    private void addBatchValues(PreparedStatement stmt, T object, List<String> allAttributesExceptId) {
        try {
            addValuesToPreparedStatement(stmt, object, allAttributesExceptId);
            stmt.addBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addValuesToPreparedStatement(PreparedStatement stmt, T o, List<String> allAttributesExceptId) throws NoSuchFieldException, IllegalAccessException, SQLException {

        for (int i = 1; i <= allAttributesExceptId.size(); i++) {
            String attributeName = allAttributesExceptId.get(i - 1);
            metadata.getAttribute(attributeName).setParameter(stmt, i, o);
        }
        metadata.id().setParameter(stmt, allAttributesExceptId.size() + 1, o);
    }
}