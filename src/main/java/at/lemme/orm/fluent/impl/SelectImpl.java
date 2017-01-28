package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Condition;
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

    private final Connection connection;
    private final Class<?> entityClass;
    private final Metadata metadata;

    private final String columnString;
    private final String wildCardString;

    private Limit limit;

    public SelectImpl(Connection connection, Class<?> clazz) {
        this.connection = connection;

        entityClass = clazz;
        metadata = Metadata.of(entityClass);
        columnString =
                metadata.getColumnNames().stream().collect(Collectors.joining(", "));
        wildCardString =
                metadata.getColumnNames().stream().map(c -> "?").collect(Collectors.joining(", "));
    }


    @Override
    public <T> Select<T> where(Condition condition) {
        return null;
    }

    @Override
    public <T> Select<T> orderBy(String attribute, String direction) {
        return null;
    }

    @Override
    public <T> Select<T> limit(int i) {
        limit = new Limit(i);
        return this;
    }

    @Override
    public <T> Select<T> limit(int start, int i) {
        return null;
    }

    @Override
    public <T> List<T> fetch() {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(columnString).append(' ');
        sql.append(" FROM ").append(metadata.getTableName());
        List<T> resultList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql.toString());
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
