package at.lemme.fluent.orm.condition;

/**
 * Created by thomas on 16.11.16.
 */
public class EqualCondition implements Condition {

    private String attribute;
    private String value;

    private EqualCondition() {
    }

    public static EqualCondition create(String attribute, String value) {
        EqualCondition condition = new EqualCondition();
        condition.attribute = attribute;
        condition.value = value;
        return condition;
    }

    @Override
    public String toSql() {
        return attribute + " = '" + value + "'";
    }

}
