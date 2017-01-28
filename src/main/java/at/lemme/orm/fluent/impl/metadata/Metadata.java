package at.lemme.orm.fluent.impl.metadata;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by thomas on 22.01.17.
 */
public class Metadata {

    private final Class<?> entityClass;
    private final String tableName;
    private final List<String> columnNames;
    private final Map<String, Column> columns;

    private Metadata(Class<?> clazz) {
        entityClass = clazz;
        tableName = clazz.getSimpleName();
        columnNames = Stream.of(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
        columns = columnNames.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        columnName -> Column.of(clazz, columnName))
                );
    }

    public static Metadata of(Class<?> clazz) {
        Metadata metadata = new Metadata(clazz);
        return metadata;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public Column getColumn(String name) {
        return columns.get(name);
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
}
