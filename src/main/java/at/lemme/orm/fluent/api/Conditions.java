package at.lemme.orm.fluent.api;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by thomas on 22.01.17.
 */
public class Conditions {

    public static Condition and(Condition... conditions) {
        return parameters -> {
            if (conditions.length > 0) {
                StringBuilder joined = new StringBuilder();
                joined.append('(');
                joined.append(Stream.of(conditions)
                        .map(condition -> condition.toSql(parameters))
                        .collect(Collectors.joining(") AND (")));
                joined.append(')');
                return joined.toString();
            } else {
                return Condition.empty().toSql(parameters);
            }
        };
    }

    public static Condition or(Condition... conditions) {
        return parameters -> {
            if (conditions.length > 0) {
                StringBuilder joined = new StringBuilder();
                joined.append('(');
                joined.append(Stream.of(conditions)
                        .map(condition -> condition.toSql(parameters))
                        .collect(Collectors.joining(") OR (")));
                joined.append(')');
                return joined.toString();
            } else {
                return Condition.empty().toSql(parameters);
            }
        };
    }

    public static Condition isNull(String column) {
        return parameters -> "(" + column + " IS NULL)";
    }

    public static Condition isNotNull(String column) {
        return parameters -> "(" + column + " IS NOT NULL)";
    }

    public static Condition between(String column, Object value1, Object value2) {
        return parameters -> {
            parameters.add(value1);
            parameters.add(value2);
            return "(" + column + " BETWEEN ? AND ?)";
        };
    }

    public static Condition in(String column, Object... values) {
        return parameters -> {
            if (values.length > 0) {
                StringBuilder joined = new StringBuilder();
                joined.append('(').append(column).append(" IN (");
                joined.append(Stream.of(values)
                        .map(o -> {
                            parameters.add(o);
                            return "?";
                        })
                        .collect(Collectors.joining(",")));
                joined.append(')').append(')');
                return joined.toString();
            } else {
                return Condition.empty().toSql(parameters);
            }
        };
    }

    public static Condition notIn(String column, Object... values) {
        return parameters -> {
            if (values.length > 0) {
                StringBuilder joined = new StringBuilder();
                joined.append('(').append(column).append(" NOT IN (");
                joined.append(Stream.of(values)
                        .map(o -> {
                            parameters.add(o);
                            return "?";
                        })
                        .collect(Collectors.joining(",")));
                joined.append(')').append(')');
                return joined.toString();
            } else {
                return Condition.empty().toSql(parameters);
            }
        };
    }

    public static Condition equals(String column, Object value) {
        return parameters -> {
            parameters.add(value);
            return "(" + column + " = ?)";
        };
    }

    public static Condition notEquals(String column, Object value) {
        return parameters -> {
            parameters.add(value);
            return "(" + column + " <> ?)";
        };
    }

    public static Condition lowerThan(String column, Object value) {
        return parameters -> {
            parameters.add(value);
            return "(" + column + " < ?)";
        };
    }

    public static Condition lowerThanOrEquals(String column, Object value) {

        return parameters -> {
            parameters.add(value);
            return "(" + column + " <= ?)";
        };
    }

    public static Condition greaterThan(String column, Object value) {

        return parameters -> {
            parameters.add(value);
            return "(" + column + " > ?)";
        };
    }

    public static Condition greaterThanOrEquals(String column, Object value) {
        return parameters -> {
            parameters.add(value);
            return "(" + column + " >= ?)";
        };
    }

    public static Condition like(String column, String pattern) {
        return parameters -> {
            parameters.add(pattern);
            return "(" + column + " LIKE ?)";
        };
    }

}
