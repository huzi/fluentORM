package at.lemme.fluent.orm.test;

import at.lemme.fluent.orm.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 22.01.17.
 */
public class TestDelete extends BaseDbTest {

    @Test
    public void testDeletePerson() throws SQLException {
        // GIVEN
        String id = "id0";

        // WHEN
        fluent.delete(Person.class).where(Conditions.equals("id", id)).execute();

        //THEN
        ResultSet rs = connection.createStatement().executeQuery("SELECT count(*) FROM Person WHERE id='" + id + "';");
        rs.next();
        int count = rs.getInt(1);
        assertThat(count).isEqualTo(0);
    }


    @Test
    public void testDeleteByObject() throws SQLException {
        // GIVEN
        Person person = (Person) fluent.select(Person.class).where(Conditions.equals("id", "id0")).fetch().get(0);

        // WHEN
        fluent.deleteObject(person).execute();

        //THEN
        ResultSet rs = connection.createStatement().executeQuery("SELECT count(*) FROM Person WHERE id='" + person.getId() + "';");
        rs.next();
        int count = rs.getInt(1);
        assertThat(count).isEqualTo(0);
    }


}
