package at.lemme.orm.fluent.test.relations;

import at.lemme.orm.fluent.BaseDbTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class TestInsertRelations extends BaseDbTest {

    @Test
    public void testSimpleOneToMany() throws SQLException {
        // GIVEN
        Customer customer = new Customer(1, "Thomas", new ArrayList<>());

        // WHEN
        fluent.insert(customer).execute();

        // THEN
    }

    @Test
    public void testSimpleManyToOne() throws SQLException {
        // GIVEN
        Item item = new Item(1, "My Item", "My description", new TodoList(100, null, null, null));

        // WHEN
        fluent.insert(item).execute();

        // THEN
        ResultSet rs = connection.createStatement().executeQuery("SELECT todoListId FROM Item WHERE id='1'");
        rs.next();
        int todoListId = rs.getInt(1);
        assertThat(todoListId).isEqualTo(100);
    }

}
