package at.lemme.orm.fluent.impl.select;

import at.lemme.orm.fluent.api.Order;
import at.lemme.orm.fluent.impl.metadata.Metadata;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class OrderBy {
    String column;
    Order order;

    public OrderBy(Metadata metadata, String attribute, Order order) {
        this.column = metadata.columnForAttribute(attribute);
        this.order = order;
    }

    public String column() {
        return column;
    }

    public Order order() {
        return order;
    }
}