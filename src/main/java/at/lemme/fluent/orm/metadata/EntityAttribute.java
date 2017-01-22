package at.lemme.fluent.orm.metadata;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.time.LocalDate;

/**
 * Created by thomas on 12.11.16.
 */
public class EntityAttribute {

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

    public Object getValue(Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStringRepresentation(Object object) {
        try {
            Object obj = field.get(object);
            if (field.getType().equals(String.class)) {
                return (String) obj;
            } else if (field.getType().equals(LocalDate.class)) {
                return ((LocalDate) obj).toString();
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Could not convert field to String! " + field.getName() + "," + field.getType());
    }

    public String getName() {
        return field.getName();
    }
}
