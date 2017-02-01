package at.lemme.orm.fluent.api;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by thomas on 22.01.17.
 */
public class Conditions {

    public static Condition and(Condition... conditions) {
        return (metadata, parameters) -> {
            if (conditions.length > 0) {
                StringBuilder joined = new StringBuilder();
                joined.append('(');
                joined.append(Stream.of(conditions)
                        .map(condition -> condition.toSql(metadata, parameters))
                        .collect(Collectors.joining(") AND (")));
                joined.append(')');
                return joined.toString();
            } else {
                return Condition.empty().toSql(metadata, parameters);
            }
        };
    }

    public static Condition or(Condition... conditions) {
        return (metadata, parameters) -> {
            if (conditions.length > 0) {
                StringBuilder joined = new StringBuilder();
                joined.append('(');
                joined.append(Stream.of(conditions)
                        .map(condition -> condition.toSql(metadata, parameters))
                        .collect(Collectors.joining(") OR (")));
                joined.append(')');
                return joined.toString();
            } else {
                return Condition.empty().toSql(metadata, parameters);
            }
        };
    }

    public static Condition isNull(String attribute) {
        return (metadata, parameters) -> "(" + attribute + " IS NULL)";
    }

    public static Condition isNotNull(String attribute) {
        return (metadata, parameters) -> "(" + attribute + " IS NOT NULL)";
    }

    public static Condition between(String attribute, Object value1, Object value2) {
        return (metadata, parameters) -> {
            parameters.add(value1);
            parameters.add(value2);
            return "(" + attribute + " BETWEEN ? AND ?)";
        };
    }

    public static Condition in(String attribute, Object... values) {
        return (metadata, parameters) -> {
            if (values.length > 0) {
                StringBuilder joined = new StringBuilder();
                joined.append('(').append(attribute).append(" IN (");
                joined.append(Stream.of(values)
                        .map(o -> {
                            parameters.add(o);
                            return "?";
                        })
                        .collect(Collectors.joining(",")));
                joined.append(')').append(')');
                return joined.toString();
            } else {
                return Condition.empty().toSql(metadata, parameters);
            }
        };
    }

    public static Condition notIn(String attribute, Object... values) {
        return (metadata, parameters) -> {
            if (values.length > 0) {
                StringBuilder joined = new StringBuilder();
                joined.append('(').append(attribute).append(" NOT IN (");
                joined.append(Stream.of(values)
                        .map(o -> {
                            parameters.add(o);
                            return "?";
                        })
                        .collect(Collectors.joining(",")));
                joined.append(')').append(')');
                return joined.toString();
            } else {
                return Condition.empty().toSql(metadata, parameters);
            }
        };
    }

    public static Condition equals(String attribute, Object value) {
        return (metadata, parameters) -> {
            parameters.add(value);
            return "(" + attribute + " = ?)";
        };
    }

    public static Condition notEquals(String attribute, Object value) {
        return (metadata, parameters) -> {
            parameters.add(value);
            return "(" + attribute + " <> ?)";
        };
    }

    public static Condition lowerThan(String attribute, Object value) {
        return (metadata, parameters) -> {
            parameters.add(value);
            return "(" + attribute + " < ?)";
        };
    }

    public static Condition lowerThanOrEquals(String attribute, Object value) {

        return (metadata, parameters) -> {
            parameters.add(value);
            return "(" + attribute + " <= ?)";
        };
    }

    public static Condition greaterThan(String attribute, Object value) {

        return (metadata, parameters) -> {
            parameters.add(value);
            return "(" + attribute + " > ?)";
        };
    }

    public static Condition greaterThanOrEquals(String attribute, Object value) {
        return (metadata, parameters) -> {
            parameters.add(value);
            return "(" + attribute + " >= ?)";
        };
    }

    public static Condition like(String attribute, String pattern) {
        return (metadata, parameters) -> {
            parameters.add(pattern);
            return "(" + metadata.getAttribute(attribute).getName() + " LIKE ?)";
        };
    }

}
