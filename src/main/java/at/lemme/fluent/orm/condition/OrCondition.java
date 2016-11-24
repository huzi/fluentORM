package at.lemme.fluent.orm.condition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thomas on 16.11.16.
 */
public class OrCondition implements Condition {

    private List<Condition> conditions;

    private OrCondition() {
    }

    public static OrCondition create(Condition... conditions) {
        OrCondition condition = new OrCondition();
        condition.conditions = Arrays.asList(conditions);
        return condition;
    }

    @Override
    public String toSql() {
        String sql = conditions.stream().map(c -> c.toSql()).collect(Collectors.joining(" OR "));
        return sql;
    }

}
