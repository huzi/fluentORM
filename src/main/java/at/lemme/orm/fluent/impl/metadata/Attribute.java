package at.lemme.orm.fluent.impl.metadata;

import at.lemme.orm.fluent.api.annotation.Column;
import at.lemme.orm.fluent.api.annotation.Id;
import at.lemme.orm.fluent.api.annotation.ManyToOne;
import at.lemme.orm.fluent.api.annotation.OneToMany;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by thomas on 27.01.17.
 */
public class Attribute {

    private final Field field;
    private final String name;
    private final String columnName;
    private final Optional<Relation> relation;

    private Attribute(Field declaredField) {
        this.name = declaredField.getName();
        declaredField.setAccessible(true);
        field = declaredField;
        columnName = extractColumnName(declaredField);
        if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToMany.class)) {
            relation = Optional.of(Relation.of(this));
        } else {
            relation = Optional.empty();
        }
    }

    private String extractColumnName(Field declaredField) {
        if (declaredField.isAnnotationPresent(Column.class) &&
                declaredField.getAnnotation(Column.class).name().length() > 0) {
            return declaredField.getAnnotation(Column.class).name();
        } else if (declaredField.isAnnotationPresent(ManyToOne.class) && declaredField.getAnnotation(ManyToOne.class).column().length() > 0) {
            return declaredField.getAnnotation(ManyToOne.class).column();
        } else {
            return declaredField.getName();
        }
    }

    public static Attribute of(Field field) {
        try {
            return new Attribute(field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setParameter(PreparedStatement stmt, int parameterIndex, Object o) {
        try {
            Object value = field.get(o);
            setValueToStatement(stmt, parameterIndex, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setValueToStatement(PreparedStatement stmt, int parameterIndex, Object value) throws SQLException {
        if (field.getType().equals(String.class)) {
            stmt.setString(parameterIndex, (String) value);
        } else if (field.getType().equals(LocalDate.class)) {
            stmt.setDate(parameterIndex, Date.valueOf((LocalDate) value));
        } else if (field.getType().equals(LocalDateTime.class)) {
            stmt.setTimestamp(parameterIndex, Timestamp.valueOf((LocalDateTime) value));
        } else if (isInt()) {
            stmt.setInt(parameterIndex, (int) value);
        } else if (isLong()) {
            stmt.setLong(parameterIndex, (long) value);
        } else if (isShort()) {
            stmt.setShort(parameterIndex, (short) value);
        } else if (isFloat()) {
            stmt.setFloat(parameterIndex, (float) value);
        } else if (isDouble()) {
            stmt.setDouble(parameterIndex, (double) value);
        }
    }

    public boolean isRelation() {
        return relation.isPresent();
    }

    public Optional<Relation> relation() {
        return relation;
    }

    private boolean isDouble() {
        return field.getType().equals(double.class) || field.getType().equals(Double.class);
    }

    private boolean isFloat() {
        return field.getType().equals(float.class) || field.getType().equals(Float.class);
    }

    private boolean isShort() {
        return field.getType().equals(short.class) || field.getType().equals(Short.class);
    }

    private boolean isLong() {
        return field.getType().equals(long.class) || field.getType().equals(Long.class);
    }

    private boolean isInt() {
        return field.getType().equals(int.class) || field.getType().equals(Integer.class);
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
            } else if (isInt()) {
                value = resultSet.getInt(columnName);
            } else if (isLong()) {
                value = resultSet.getLong(columnName);
            } else if (isShort()) {
                value = resultSet.getShort(columnName);
            } else if (isFloat()) {
                value = resultSet.getFloat(columnName);
            } else if (isDouble()) {
                value = resultSet.getDouble(columnName);
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

    Field field() {
        return field;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", columnName='" + columnName + '\'' +
                '}';
    }
}
