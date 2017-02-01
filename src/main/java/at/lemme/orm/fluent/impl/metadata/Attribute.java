package at.lemme.orm.fluent.impl.metadata;

import at.lemme.orm.fluent.api.annotation.Column;
import at.lemme.orm.fluent.api.annotation.Id;

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
public class Attribute {

    private final Field field;
    private final String name;
    private final String columnName;

    private Attribute(String attributeName, Field declaredField) {
        this.name = attributeName;
        declaredField.setAccessible(true);
        field = declaredField;
        columnName = extractColumnName(declaredField);
    }

    private String extractColumnName(Field declaredField) {
        if (declaredField.isAnnotationPresent(Column.class) &&
                declaredField.getAnnotation(Column.class).name().length() > 0) {
            return declaredField.getAnnotation(Column.class).name();
        } else {
            return declaredField.getName();
        }
    }

    public static Attribute of(Class<?> clazz, String attributeName) {
        try {
            return new Attribute(attributeName, clazz.getDeclaredField(attributeName));
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

    public Object getValue(Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String name() {
        return name;
    }

    public String columnName() {
        return columnName;
    }

    boolean hasIdAnnotation() {
        return field.isAnnotationPresent(Id.class);
    }
}
