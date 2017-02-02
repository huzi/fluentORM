package at.lemme.orm.fluent.impl.metadata;

import at.lemme.orm.fluent.api.annotation.Table;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by thomas on 22.01.17.
 */
public class Metadata {

    private static Map<Class<?>, Metadata> metadatas = new HashMap<>();

    private final Class<?> entityClass;
    private final String tableName;
    private final List<Attribute> attributes;
    private final Map<String, Attribute> attributeMap;
    private final Attribute idAttribute;
    private final List<Relation> relations;

    private Metadata(Class<?> clazz) {
        metadatas.put(clazz, this);
        entityClass = clazz;
        tableName = extractTableName(clazz);
        attributes = Stream.of(clazz.getDeclaredFields()).map(Attribute::of).collect(Collectors.toList());
        attributeMap = attributes.stream().collect(Collectors.toMap(Attribute::name, Function.identity()));
        idAttribute = extractId(attributeMap.values());
        relations = attributes.stream().map(Attribute::relation).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
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
        if (metadatas.containsKey(clazz)) {
            return metadatas.get(clazz);
        } else {
            return new Metadata(clazz);
        }
    }

    public String tableName() {
        return tableName;
    }

    public Attribute id() {
        return idAttribute;
    }

    public List<String> columnNames() {
        return attributes.stream().filter(attribute -> !(attribute.isRelation() && attribute.relation().get().type.equals(Relation.Type.OneToMany)))
                .map(Attribute::columnName).collect(Collectors.toList());
    }

    public List<Attribute> columnAttributes() {
        return attributes.stream().filter(attribute -> !(attribute.isRelation() && attribute.relation().get().type.equals(Relation.Type.OneToMany)))
                .collect(Collectors.toList());
    }

    public List<String> attributeNames() {
        return attributes.stream().map(Attribute::name).collect(Collectors.toList());
    }

    public List<Relation> relations() {
        return relations;
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

    @Override
    public String toString() {
        return "Metadata{" +
                "\nentityClass=" + entityClass +
                ",\n tableName='" + tableName + '\'' +
                ",\n attributes=" + attributes +
                ",\n attributeMap=" + attributeMap +
                ",\n idAttribute=" + idAttribute +
                ",\n columnNames=" + columnNames() +
                ",\n relations=" + relations +
                '}';
    }
}
