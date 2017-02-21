package at.lemme.orm.fluent.test.relations;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class TestSelectRelations extends BaseDbTest {

    @Test
    public void testOneToMany() {
        // GIVEN
        Customer customer = new Customer(1, "Thomas", new ArrayList<>());
        Customer customer2 = new Customer(2, "Maxi", new ArrayList<>());
        TodoList todoList1 = new TodoList(1, "Thomas List1", customer, new ArrayList<>());
        TodoList todoList2 = new TodoList(2, "Thomas List2", customer, new ArrayList<>());
        fluent.insert(customer).execute();
        fluent.insert(customer2).execute();
        fluent.insert(todoList1).execute();
        fluent.insert(todoList2).execute();

        // WHEN
        List<Customer> list = fluent.select(Customer.class).with("lists").fetch();

        // THEN
        System.out.println(list);
        assertThat(list).hasSize(2);
        final Customer customer1 = list.stream().filter(c -> c.getId() == 1).findFirst().get();
        assertThat(customer1.getName()).isEqualTo("Thomas");
        assertThat(customer1.getLists()).hasSize(2);
        assertThat(customer1.getLists().get(0).getCustomer()).isEqualTo(customer1);
    }

    @Test
    public void testManyToOne() {
        // GIVEN
        Customer customer = new Customer(1, "Thomas", new ArrayList<>());
        Customer customer2 = new Customer(2, "Maxi", new ArrayList<>());
        TodoList todoList1 = new TodoList(1, "Thomas List1", customer, new ArrayList<>());
        TodoList todoList2 = new TodoList(2, "Thomas List2", customer, new ArrayList<>());
        fluent.insert(customer).execute();
        fluent.insert(customer2).execute();
        fluent.insert(todoList1).execute();
        fluent.insert(todoList2).execute();

        // WHEN
        List<TodoList> list = fluent.select(TodoList.class).with("customer").fetch();

        // THEN
        System.out.println(list);
        assertThat(list).hasSize(2);
        final TodoList todoList = list.stream().filter(tl -> tl.getId() == 1).findFirst().get();
        assertThat(todoList.getTitle()).isEqualTo("Thomas List1");
        assertThat(todoList.getCustomer().getName()).isEqualTo("Thomas");
    }

}
