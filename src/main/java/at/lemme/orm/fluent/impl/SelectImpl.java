package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Condition;
import at.lemme.orm.fluent.api.Order;
import at.lemme.orm.fluent.api.Select;
import at.lemme.orm.fluent.impl.metadata.Metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thomas on 22.01.17.
 */
public class SelectImpl<T> implements Select<T> {

    private class Limit {
        int limit = 1000;
        int skip = 0;

        public Limit(int limit) {
            this.limit = limit;
        }

        public Limit(int limit, int skip) {
            this.limit = limit;
            this.skip = skip;
        }
    }

    private class OrderBy {
        String column;
        Order order;

        public OrderBy(String attribute, Order order) {
            this.column = metadata.columnForAttribute(attribute);
            this.order = order;
        }
    }

    private final Connection connection;
    private final Class<?> entityClass;
    private final Metadata metadata;

    private final String attributeString;

    private Limit limit;
    private OrderBy order;
    private Condition condition = Condition.empty();

    public SelectImpl(Connection connection, Class<?> clazz) {
        this.connection = connection;

        entityClass = clazz;
        metadata = Metadata.of(entityClass);
        attributeString =
                metadata.columnNames().stream().collect(Collectors.joining(", "));
    }

    @Override
    public <T> Select<T> where(Condition condition) {
        this.condition = condition;
        return (Select<T>) this;
    }

    @Override
    public <T> Select<T> orderBy(String attribute, Order order) {
        this.order = new OrderBy(attribute, order);
        return (Select<T>) this;
    }

    @Override
    public <T> Select<T> limit(int start) {
        limit = new Limit(start);
        return (Select<T>) this;
    }

    @Override
    public <T> Select<T> limit(int start, int skip) {
        limit = new Limit(start, skip);
        return (Select<T>) this;
    }

    @Override
    public <T> List<T> fetch() {
        Parameters parameters = new Parameters();

        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(attributeString).append(' ');
        sql.append(" FROM ").append(metadata.tableName());
        sql.append(" WHERE ").append(condition.toSql(metadata, parameters));
        if (order != null) {
            sql.append(" ORDER BY ").append(order.column).append(' ').append(order.order);
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit.limit);
            sql.append(" OFFSET ").append(limit.skip);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            parameters.apply(stmt);
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
}
