package at.lemme.orm.fluent.test.relations;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.test.relations.model.Address;
import at.lemme.orm.fluent.test.relations.model.Customer;
import at.lemme.orm.fluent.test.relations.model.TodoList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class TestSelectRelations extends BaseDbTest {

    @Test
    public void testOneToManySingleRelation() {
        // GIVEN
        insertTestScenario();

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
    public void testOneToManyMultipleRelations() {
        // GIVEN
        insertTestScenario();

        // WHEN
        List<Customer> list = fluent.select(Customer.class).with("lists", "addresses").fetch();

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
        insertTestScenario();

        // WHEN
        List<TodoList> list = fluent.select(TodoList.class).with("customer").fetch();

        // THEN
        final TodoList todoList1 = list.stream().filter(tl -> tl.getId() == 1).findFirst().get();
        final TodoList todoList2 = list.stream().filter(tl -> tl.getId() == 2).findFirst().get();
        assertThat(list).hasSize(2);
        assertThat(todoList1.getTitle()).isEqualTo("Thomas List1");
        assertThat(todoList1.getCustomer().getName()).isEqualTo("Thomas");
        assertThat(todoList2.getTitle()).isEqualTo("Thomas List2");
        assertThat(todoList2.getCustomer().getName()).isEqualTo("Thomas");
    }

    /**
     * - Thomas
     *    |- Thomas List1
     *    |- Thomas List2
     * - Maxi
     */
    private void insertTestScenario() {
        Customer customer1 = new Customer(1, "Thomas", new ArrayList<>());
        Address address1 = new Address("address1", "My Street 1", "My City 1", "Austria", customer1);
        Address address2 = new Address("address2", "My Street 2", "My City 2", "Austria", customer1);
        Address address3 = new Address("address3", "My Street 3", "My City 3", "Austria", customer1);
        Customer customer2 = new Customer(2, "Maxi", new ArrayList<>());
        TodoList todoList1 = new TodoList(1, "Thomas List1", customer1, new ArrayList<>());
        TodoList todoList2 = new TodoList(2, "Thomas List2", customer1, new ArrayList<>());
        fluent.insert(customer1).execute();
        fluent.insert(address1).execute();
        fluent.insert(address2).execute();
        fluent.insert(address3).execute();
        fluent.insert(customer2).execute();
        fluent.insert(todoList1).execute();
        fluent.insert(todoList2).execute();
    }

}
