package at.lemme.orm.fluent.impl.metadata;

import at.lemme.orm.fluent.api.annotation.Table;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by thomas on 22.01.17.
 */
public class Metadata {

    private final Class<?> entityClass;
    private final String tableName;
    private final List<String> attributeNames;
    private final Map<String, Attribute> attributeMap;
    private final Attribute idAttribute;
    private final List<String> columnNames;

    private Metadata(Class<?> clazz) {
        entityClass = clazz;
        tableName = extractTableName(clazz);
        attributeNames = Stream.of(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
        attributeMap = attributeNames.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        name -> Attribute.of(clazz, name))
                );
        columnNames = attributeNames.stream()
                .map(attributeName->attributeMap.get(attributeName).columnName())
                .collect(Collectors.toList());
        idAttribute = extractId(attributeMap.values());
    }

    private String extractTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            String name = clazz.getAnnotation(Table.class).name();
            if (name.length() > 0) {
                return name;
            }
        }
        return clazz.getSimpleName();
    }

    private Attribute extractId(Collection<Attribute> attributes) {
        Optional<Attribute> idAnnotationOptional = attributes.stream().filter(Attribute::hasIdAnnotation).findFirst();
        Optional<Attribute> idAttributeNameOptional = attributes.stream().filter(attribute -> attribute.name().equals("id")).findFirst();
        if (idAnnotationOptional.isPresent()) {
            return idAnnotationOptional.get();
        } else if (idAttributeNameOptional.isPresent()) {
            return idAttributeNameOptional.get();
        } else {
            throw new RuntimeException("No id attribute found on class " + entityClass);
        }
    }

    public static Metadata of(Class<?> clazz) {
        Metadata metadata = new Metadata(clazz);
        return metadata;
    }

    public String tableName() {
        return tableName;
    }

    public Attribute id(){
        return idAttribute;
    }

    public List<String> columnNames() {
        return columnNames;
    }
    public List<String> attributeNames() {
        return attributeNames;
    }

    public Attribute getAttribute(String name) {
        return attributeMap.get(name);
    }

    public String columnForAttribute(String name) {
        return attributeMap.get(name).columnName();
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
}
