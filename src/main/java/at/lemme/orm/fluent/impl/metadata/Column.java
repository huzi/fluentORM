package at.lemme.orm.fluent.impl.metadata;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by thomas on 27.01.17.
 */
public class Column {

    private final Field field;
    private final String columnName;

    private Column(String columnName, Field declaredField) {
        this.columnName = columnName;
        declaredField.setAccessible(true);
        field = declaredField;
    }

    public static Column of(Class<?> clazz, String columnName) {
        try {
            return new Column(columnName, clazz.getDeclaredField(columnName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setParameter(PreparedStatement stmt, int i, Object o) {
        try {
            Object value = field.get(o);
            if (field.getType().equals(String.class)) {
                stmt.setString(i, (String) value);
            } else if (field.getType().equals(LocalDate.class)) {
                stmt.setDate(i, Date.valueOf((LocalDate) value));
            } else if (field.getType().equals(LocalDateTime.class)) {
                stmt.setTimestamp(i, Timestamp.valueOf((LocalDateTime) value));
            } else if (field.getType().equals(int.class)) {
                stmt.setInt(i, (int) value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAttribute(Object obj, ResultSet resultSet) {
        Object value = null;
        try {
            if (field.getType().equals(String.class)) {
                value = resultSet.getString(columnName);
            } else if (field.getType().equals(LocalDate.class)) {
                value = resultSet.getDate(columnName).toLocalDate();
            } else if (field.getType().equals(LocalDateTime.class)) {
                value = resultSet.getTimestamp(columnName).toLocalDateTime();
            } else if (field.getType().equals(int.class)) {
                value = resultSet.getInt(columnName);
            }
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
