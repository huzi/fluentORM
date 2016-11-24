package at.lemme.fluent.orm;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thomas on 11.11.16.
 */
public class SaveByObject<T> {

    private final T object;
    private final SecureRandom random = new SecureRandom();

    private Save delegate;

    public SaveByObject(final Save save, final T object) {
        this.delegate = save;
        this.object = object;
    }


    public T execute() {
        List<EntityAttribute> attributes = getEntityAttributes();
        String fieldString = attributes.stream().map(a -> a.getName()).collect(Collectors.joining(", "));
        String contentString = attributes.stream()
                .map(a -> a.isId() ? nextRandomId() : a.getStringRepresentation(object))
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(","));

        String query = buildInsertQuery(fieldString, contentString);

        try {
            PreparedStatement stmt = delegate.connection.prepareStatement(query);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    private String buildInsertQuery(String fieldString, String contentString) {
        String s = "INSERT INTO " + delegate.tableName + "(" + fieldString + ")" + " VALUES (" + contentString + ")";
        return s;
    }

    private List<EntityAttribute> getEntityAttributes() {
        Field[] fields = delegate.entityClass.getDeclaredFields();
        List<EntityAttribute> attributes = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            attributes.add(EntityAttribute.create(fields[i]));
        }

        return attributes;
    }

    public String nextRandomId() {
        int length = 16;
        final String allowedChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder pass = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            pass.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return pass.toString();
    }


}
