package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Insert;
import at.lemme.orm.fluent.impl.metadata.Attribute;
import at.lemme.orm.fluent.impl.metadata.Metadata;
import at.lemme.orm.fluent.impl.metadata.Relation;

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

    private final String attributeString;
    private final String wildCardString;

    public InsertImpl(Connection connection, List<T> objects) {
        this.connection = connection;
        this.objects = new ArrayList<T>(objects);

        entityClass = objects.get(0).getClass();
        metadata = Metadata.of(entityClass);
        attributeString =
                metadata.columnNames().stream().collect(Collectors.joining(", "));
        wildCardString =
                metadata.columnNames().stream().map(c -> "?").collect(Collectors.joining(", "));
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
        sql.append(metadata.tableName());
        sql.append('(').append(attributeString).append(')');
        sql.append(" VALUES ");
        sql.append('(').append(wildCardString).append(')');
        return sql;
    }

    private void addBatchValues(PreparedStatement stmt, T object) {
        try {
            addValuesToPreparedStatement(stmt, object);
            System.out.println(stmt);
            stmt.addBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addValuesToPreparedStatement(PreparedStatement stmt, T o) throws NoSuchFieldException, IllegalAccessException, SQLException {

        int index = 1;
        for (String attributeName : metadata.attributeNames()) {
            final Attribute attribute = metadata.getAttribute(attributeName);
            if (!attribute.isRelation()) {
                attribute.setParameter(stmt, index, o);
                index++;
            } else if (attribute.relation().get().type().equals(Relation.Type.ManyToOne)) {
                final Relation relation = attribute.relation().get();
                Object value = attribute.getValue(o);
                relation.referencedMetadata().id().setParameter(stmt, index, value);
                index++;
            }
        }

    }
}
