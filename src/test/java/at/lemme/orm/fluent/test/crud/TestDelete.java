package at.lemme.orm.fluent.test.crud;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        ResultSet rs = connection.createStatement().executeQuery("SELECT count(*) FROM Person WHERE column_id='" + id + "';");
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
        ResultSet rs = connection.createStatement().executeQuery("SELECT count(*) FROM Person WHERE column_id='" + person.getId() + "';");
        rs.next();
        int count = rs.getInt(1);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void testDeleteByObjects() throws SQLException {
        // GIVEN
        List<Person> persons = fluent.select(Person.class).where(Conditions.in("id", "id0", "id1")).fetch();
        Assertions.assertThat(persons).hasSize(2);

        // WHEN
        fluent.deleteObjects(persons.toArray(new Person[persons.size()])).execute();

        //THEN
        persons = fluent.select(Person.class).where(Conditions.in("id", "id0", "id1")).fetch();
        Assertions.assertThat(persons).hasSize(0);
    }


}
