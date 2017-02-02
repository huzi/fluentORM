package at.lemme.orm.fluent.test.relations;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class TestUpdateRelations extends BaseDbTest {

    @Test
    public void testSimpleOneToMany() throws SQLException {
        // GIVEN
        Customer customer = new Customer(1, "Thomas", new ArrayList<>());
        fluent.insert(customer).execute();
        customer.setName("Tommi");

        // WHEN
        fluent.update(customer).execute();

        // THEN
        Customer savedCustomer = (Customer) fluent.select(Customer.class).where(Conditions.equals("id", customer.getId())).fetch().get(0);
        assertThat(savedCustomer.getName()).isEqualTo("Tommi");
    }

    @Test
    public void testSimpleManyToOne() throws SQLException {
        // GIVEN
        Item item = new Item(1, "My Item", "My description", new TodoList(100, null, null, null));
        fluent.insert(item).execute();
        item.setTitle("Your Item");

        // WHEN
        fluent.update(item).execute();

        // THEN
        Item savedItem = (Item) fluent.select(Item.class).where(Conditions.equals("id", item.getId())).fetch().get(0);
        assertThat(savedItem.getTitle()).isEqualTo("Your Item");
    }

}
