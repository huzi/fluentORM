package at.lemme.orm.fluent.test.relations;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class TestDeleteRelations extends BaseDbTest {

    @Test
    public void testSimpleOneToMany() throws SQLException {
        // GIVEN
        Customer customer = new Customer(1, "Thomas", new ArrayList<>());
        fluent.insert(customer).execute();

        // WHEN
        fluent.delete(Customer.class).where(Conditions.equals("id", 1)).execute();

        // THEN
        List<Customer> result = fluent.select(Customer.class).where(Conditions.equals("id", customer.getId())).fetch();
        assertThat(result).isEmpty();
    }

    @Test
    public void testSimpleOneToManyByObject() throws SQLException {
        // GIVEN
        Customer customer = new Customer(1, "Thomas", new ArrayList<>());
        fluent.insert(customer).execute();

        // WHEN
        fluent.deleteObject(customer).execute();

        // THEN
        List<Customer> result = fluent.select(Customer.class).where(Conditions.equals("id", customer.getId())).fetch();
        assertThat(result).isEmpty();
    }

    @Test
    public void testSimpleManyToOne() throws SQLException {
        // GIVEN
        Item item = new Item(1, "My Item", "My description", new TodoList(100, null, null, null));
        fluent.insert(item).execute();

        // WHEN
        fluent.delete(Item.class).where(Conditions.equals("id", 1)).execute();

        // THEN
        List<Item> result = fluent.select(Item.class).where(Conditions.equals("id", item.getId())).fetch();
        assertThat(result).isEmpty();
    }

    @Test
    public void testSimpleManyToOneByObject() throws SQLException {
        // GIVEN
        Item item = new Item(1, "My Item", "My description", new TodoList(100, null, null, null));
        fluent.insert(item).execute();
        // WHEN
        fluent.deleteObject(item).execute();

        // THEN
        List<Item> result = fluent.select(Item.class).where(Conditions.equals("id", item.getId())).fetch();
        assertThat(result).isEmpty();
    }

}
