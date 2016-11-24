package at.lemme.fluent.orm;

import javax.persistence.Id;
import java.lang.reflect.Field;

/**
 * Created by thomas on 12.11.16.
 */
public class EntityAttribute<T> {

    private final Field field;

    private EntityAttribute(Field field) {
        this.field = field;
        field.setAccessible(true);
    }

    public static EntityAttribute create(Field field) {
        return new EntityAttribute(field);
    }


    public boolean isId() {
        return field.isAnnotationPresent(Id.class);
    }

    public Object getValue(T object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStringRepresentation(T object) {
        try {
            return (String) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return field.getName();
    }
}
