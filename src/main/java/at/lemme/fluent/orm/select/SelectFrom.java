package at.lemme.fluent.orm.select;

import at.lemme.fluent.orm.condition.Condition;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 11.11.16.
 */
public class SelectFrom<T> {

    private final Select select;
    private final Class<T> entityClass;
    private String alias;
    private Condition condition;

    public SelectFrom(final Select select, Class<T> entityClass) {
        this.entityClass = entityClass;
        this.select = select;
    }

    public SelectFrom<T> alias(String alias) {
        this.alias = alias;
        return this;
    }

    public SelectFrom<T> where(Condition condition) {
        this.condition = condition;
        return this;
    }

    public List<T> list() {
        String query = buildSelectAllQuery();
        ResultSet rs = executeQuery(query);
        List<T> resultList = resultSetToList(rs);
        return resultList;
    }

    private String buildSelectAllQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        if (alias != null && !alias.isEmpty()) {
            query.append(alias).append(".");
        }
        query.append("* FROM ");
        query.append(select.tableName);

        if (alias != null && !alias.isEmpty()) {
            query.append(" ").append(alias);
        }

        if (condition != null) {
            query.append(" WHERE ").append(condition.toSql());
        }

        System.out.println(query.toString());

        return query.toString();
    }

    private List<T> resultSetToList(ResultSet rs) {
        Field[] fields = select.entityClass.getDeclaredFields();
        List<T> resultList = new ArrayList<>();

        try {
            while (rs.next()) {
                createEntityInstanceFromResultSetRow(rs, fields, resultList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    private void createEntityInstanceFromResultSetRow(ResultSet rs, Field[] fields, List<T> resultList) throws InstantiationException, IllegalAccessException, SQLException {
        T person = (T) select.entityClass.newInstance();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldContent = rs.getString(field.getName());
            if (field.getType().equals(String.class)) {
                field.set(person, fieldContent);
            } else if (field.getType().equals(LocalDate.class)) {
                field.set(person, LocalDate.parse(fieldContent));
            }
        }
        resultList.add(person);
    }

    private ResultSet executeQuery(String query) {
        try {
            PreparedStatement stmt = select.connection.prepareStatement(query);
            return stmt.executeQuery();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
