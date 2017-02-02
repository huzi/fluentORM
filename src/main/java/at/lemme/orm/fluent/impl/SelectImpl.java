package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Condition;
import at.lemme.orm.fluent.api.Order;
import at.lemme.orm.fluent.api.Select;
import at.lemme.orm.fluent.impl.metadata.Metadata;
import at.lemme.orm.fluent.impl.metadata.Relation;
import at.lemme.orm.fluent.impl.select.Limit;
import at.lemme.orm.fluent.impl.select.OrderBy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thomas on 22.01.17.
 */
public class SelectImpl<T> implements Select<T> {


    private final Connection connection;
    private final Class<?> entityClass;
    private final Metadata metadata;

    private final String alias;
    private final String attributeString;


    private List<Relation> fetchRelations = Collections.emptyList();
    private Limit limit;
    private OrderBy order;
    private Condition condition = Condition.empty();

    public SelectImpl(Connection connection, Class<?> clazz) {
        this.connection = connection;
        entityClass = clazz;
        metadata = Metadata.of(entityClass);
        alias = "t0";
        attributeString =
                metadata.columnNames().stream().map(c -> alias + "." + c).collect(Collectors.joining(", "));
    }

    @Override
    public Select<T> with(String relation) {
        fetchRelations = metadata.relations().stream().filter(r -> r.name().equals(relation)).collect(Collectors.toList());
        return this;
    }

    @Override
    public Select<T> where(Condition condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public Select<T> orderBy(String attribute, Order order) {
        this.order = new OrderBy(metadata, attribute, order);
        return this;
    }

    @Override
    public Select<T> limit(int limit) {
        this.limit = new Limit(limit);
        return this;
    }

    @Override
    public Select<T> limit(int limit, int offset) {
        this.limit = new Limit(limit, offset);
        return (Select<T>) this;
    }

    @Override
    public List<T> fetch() {
        Parameters parameters = new Parameters();

        StringBuilder sql = buildSql(parameters);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            parameters.apply(stmt);
            System.out.println(stmt);
            List<T> resultList = new ArrayList<>();
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    T obj = (T) metadata.getEntityClass().newInstance();
                    for (String attributeName : metadata.attributeNames()) {
                        metadata.getAttribute(attributeName).setAttribute(obj, resultSet);
                    }
                    resultList.add(obj);
                }
            }
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder buildSql(Parameters parameters) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(attributeString);

        if (!fetchRelations.isEmpty()) {
            String joinAlias = "j0";
            Relation joinAttribute = fetchRelations.get(0);
            String joinAttributes = joinAttribute.referencedMetadata().columnNames().stream()
                    .map(c -> joinAlias + "." + c).collect(Collectors.joining(", "));
            sql.append(',').append(joinAttributes);
        }

        sql.append(" FROM ").append(metadata.tableName()).append(' ').append(alias);

        if (!fetchRelations.isEmpty()) {
            String joinAlias = "j0";
            Relation joinAttribute = fetchRelations.get(0);

            sql.append(" LEFT JOIN ").append(joinAttribute.referencedColumn());
            sql.append(" ON (");
            sql.append(alias).append('.').append(joinAttribute.column());
            sql.append('=');
            sql.append(joinAlias).append('.').append(joinAttribute.referencedColumn()).append(')');
        }

        sql.append(" WHERE ").append(condition.toSql(metadata, parameters));
        if (order != null) {
            sql.append(" ORDER BY ").append(order.column()).append(' ').append(order.order());
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit.limit());
            sql.append(" OFFSET ").append(limit.offset());
        }
        return sql;
    }
}
