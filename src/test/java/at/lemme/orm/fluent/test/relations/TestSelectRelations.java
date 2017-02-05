package at.lemme.orm.fluent.test.relations;

import at.lemme.orm.fluent.BaseDbTest;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class TestSelectRelations extends BaseDbTest {

    @Test
    public void testWithRelations() {
        // GIVEN
        Customer customer = new Customer(1, "Thomas", new ArrayList<>());
        TodoList todoList1 = new TodoList(1, "Thomas List1", customer, new ArrayList<>());
        TodoList todoList2 = new TodoList(2, "Thomas List2", customer, new ArrayList<>());
        fluent.insert(customer).execute();
        fluent.insert(todoList1).execute();
        fluent.insert(todoList2).execute();

        // WHEN
        fluent.select(Customer.class).with("lists").fetch();

        // THEN
    }

}
