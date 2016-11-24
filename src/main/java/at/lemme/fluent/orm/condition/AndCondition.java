package at.lemme.fluent.orm.condition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thomas on 16.11.16.
 */
public class AndCondition implements Condition {

    private List<Condition> conditions;

    private AndCondition() {
    }

    public static AndCondition create(Condition... conditions) {
        AndCondition condition = new AndCondition();
        condition.conditions = Arrays.asList(conditions);
        return condition;
    }

    @Override
    public String toSql() {
        String sql = conditions.stream().map(c->c.toSql()).collect(Collectors.joining(" AND "));
        return sql;
    }

}
