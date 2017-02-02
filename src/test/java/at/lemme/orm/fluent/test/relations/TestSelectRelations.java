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
        TodoList todoList = new TodoList(1, "Thomas List", customer, new ArrayList<>());
        fluent.insert(customer).execute();
        fluent.insert(todoList).execute();

        // WHEN

        // THEN
    }

}
