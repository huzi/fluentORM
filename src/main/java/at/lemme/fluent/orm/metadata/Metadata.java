package at.lemme.fluent.orm.metadata;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by thomas on 24.11.16.
 */
public class Metadata {

    private final Class<?> entityClass;
    private final String tableName;
    private final Class<?> idClass;

    private final Map<String, Field> attributes;
    private final Set<String> columnNames;

    public Metadata(Class<?> clazz) {
        this.entityClass = clazz;

        // validations
        checkForNoArgConstructor(clazz);

        // retrieve metadata
        tableName = retrieveTableName(clazz);
        idClass = retrieveIdClass();

        attributes = retrieveAttributes();
        columnNames = attributes.keySet();

    }

    private Map<String, Field> retrieveAttributes() {
        return Stream.of(entityClass.getDeclaredFields())
                .collect(Collectors.toMap(this::retrieveColumnName, f -> f));
    }

    private String retrieveColumnName(Field field) {
        String columnName = retrieveColumnNameFromFieldOrAnnotation(field);
        if (field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class)) {
            columnName = getColumnNameForRelation(field, columnName);
        }
        return columnName;
    }

    private String getColumnNameForRelation(Field field, String columnName) {
        if (field.isAnnotationPresent(JoinColumn.class)) {
            columnName = field.getAnnotation(JoinColumn.class).name();
        } else {
            Class<?> targetType = field.getType();
            Field[] targetTypeFields = targetType.getDeclaredFields();
            Optional<Field> targetField = Stream.of(targetTypeFields).filter(f -> f.isAnnotationPresent(Id.class)).findFirst();
            if (targetField.isPresent()) {
                columnName = columnName + "_" + targetField.get().getName();
            } else {
                throw new RuntimeException("No Id attribute on target entity " + targetType.getName());
            }
        }
        return columnName;
    }

    private String retrieveColumnNameFromFieldOrAnnotation(Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(Column.class)) {
            columnName = field.getAnnotation(Column.class).name();
        }
        return columnName;
    }

    private void checkForNoArgConstructor(Class<?> clazz) {
        boolean hasNoArgConstructor = Arrays.stream(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
        if (!hasNoArgConstructor) {
            throw new IllegalArgumentException("The entity class has to provide a no arg constructor!");
        }
    }

    private Class<?> retrieveIdClass() {
        Optional<? extends Class<?>> fieldOptional = Arrays.stream(this.entityClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .map(f -> f.getType())
                .findFirst();
        return fieldOptional.orElse(null);
    }

    private String retrieveTableName(Class<?> clazz) {
        Optional<String> tableNameOptional = Optional.empty();
        if (clazz.isAnnotationPresent(Table.class)) {
            tableNameOptional = Optional.ofNullable(clazz.getAnnotation(Table.class).name());
        }
        return tableNameOptional.isPresent() ? tableNameOptional.get() : clazz.getSimpleName();
    }

    public String getTableName() {
        return tableName;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Class<?> getIdClass() {
        return idClass;
    }

    public Set<String> getColumns() {
        return attributes.keySet();
    }

    public Set<String> getColumnNames() {
        return columnNames;
    }
}
