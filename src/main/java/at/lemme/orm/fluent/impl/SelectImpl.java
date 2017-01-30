package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Condition;
import at.lemme.orm.fluent.api.Order;
import at.lemme.orm.fluent.api.QueryParameters;
import at.lemme.orm.fluent.api.Select;
import at.lemme.orm.fluent.impl.metadata.Metadata;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        String attribute;
        Order order;

        public OrderBy(String attribute, Order order) {
            this.attribute = attribute;
            this.order = order;
        }
    }

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

    private final String columnString;

    private Limit limit;
    private OrderBy order;
    private Condition condition = Condition.empty();

    public SelectImpl(Connection connection, Class<?> clazz) {
        this.connection = connection;

        entityClass = clazz;
        metadata = Metadata.of(entityClass);
        columnString =
                metadata.getColumnNames().stream().collect(Collectors.joining(", "));
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
        sql.append(columnString).append(' ');
        sql.append(" FROM ").append(metadata.getTableName());
        sql.append(" WHERE ").append(condition.toSql(parameters));
        if (order != null) {
            sql.append(" ORDER BY ").append(order.attribute).append(' ').append(order.order);
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit.limit);
            sql.append(" OFFSET ").append(limit.skip);
        }

        List<T> resultList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            parameters.apply(stmt);
            ResultSet resultSet = stmt.executeQuery();


            while (resultSet.next()) {
                T obj = (T) metadata.getEntityClass().newInstance();
                for (String columnName : metadata.getColumnNames()) {
                    metadata.getColumn(columnName).setAttribute(obj, resultSet);
                }
                resultList.add(obj);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
