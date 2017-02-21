package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.*;
import at.lemme.orm.fluent.impl.metadata.Metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static at.lemme.orm.fluent.impl.util.SqlTerms.*;

/**
 * Created by thomas18 on 20.02.2017.
 */
public class ProjectImpl implements Project {

    private final List<Expression> expressions;
    private final Connection connection;

    private Metadata metadata;
    private Condition condition;

    public ProjectImpl(Connection connection, List<Expression> expressions) {
        this.connection = connection;
        this.expressions = expressions;
    }

    @Override
    public Project from(Class<?> clazz) {
        this.metadata = Metadata.of(clazz);
        return this;
    }

    @Override
    public Project where(Condition condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public Project orderBy(String attribute, Order order) {
        return null;
    }

    @Override
    public Project limit(int limit) {
        return null;
    }

    @Override
    public Project limit(int limit, int offset) {
        return null;
    }

    @Override
    public <T> T mapResult(ResultMapper<T> resultMapper) {
        return null;
    }

    @Override
    public <T> List<T> mapResults(ResultMapper<T> resultMapper) {
        QueryParameters queryParameters = new Parameters();
        StringBuilder sql = new StringBuilder(SELECT).append(WHITESPACE);

        expressions.forEach(e -> sql.append(e.toString()));

        sql.append(FROM).append(WHITESPACE);
        sql.append(metadata.tableName());
        if (condition != null) {
            sql.append(WHERE).append(WHITESPACE).append(condition.toSql(metadata, queryParameters));
        }


        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            queryParameters.apply(stmt);
            List<T> resultList = new ArrayList<T>();
            try (ResultSet resultSet = stmt.executeQuery()) {
                int index = 1;
                while (resultSet.next()) {
                    resultList.add(resultMapper.mapResult(resultSet, index));
                    index++;
                }
            }
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> list(Class<T> T) {
        return null;
    }
}
