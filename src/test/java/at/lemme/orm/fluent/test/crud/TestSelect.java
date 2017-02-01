package at.lemme.orm.fluent.test.crud;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import at.lemme.orm.fluent.api.Order;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 22.01.17.
 */
public class TestSelect extends BaseDbTest {

    @Test
    public void testSelectPerson() throws SQLException {
        // WHEN
        List<Person> list = fluent.select(Person.class).fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
    }

    @Test
    public void testLimit() throws SQLException {
        // WHEN
        List<Person> list = fluent.select(Person.class).limit(3).fetch();

        //THEN
        Assertions.assertThat(list).hasSize(3);
        assertThat(list.get(0).getId()).isNotEmpty();
        assertThat(list.get(0).getFirstName()).isNotEmpty();
        assertThat(list.get(0).getLastName()).isNotEmpty();
        assertThat(list.get(0).getLastLogin()).isNotNull();
        assertThat(list.get(0).getBirthDate()).isNotNull();

    }

    @Test
    public void testLimitOffset() throws SQLException {
        // GIVEN
        String sql = "SELECT COUNT(*) FROM Person;";
        ResultSet rs = connection.createStatement().executeQuery(sql);
        rs.next();
        int count = rs.getInt(1);
        System.out.println(count);

        // WHEN
        List<Person> list = fluent.select(Person.class).limit(3, count - 1).fetch();

        //THEN
        Assertions.assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isNotEmpty();
        assertThat(list.get(0).getFirstName()).isNotEmpty();
        assertThat(list.get(0).getLastName()).isNotEmpty();
        assertThat(list.get(0).getLastLogin()).isNotNull();
        assertThat(list.get(0).getBirthDate()).isNotNull();

    }

    @Test
    public void testOrderBy() {
        // GIVEN

        // WHEN
        List<Person> list = fluent.select(Person.class).orderBy("birthDate", Order.ASC).limit(3).fetch();

        //THEN
        assertThat(list.get(0).getBirthDate()).isBeforeOrEqualTo(list.get(1).getBirthDate());
        assertThat(list.get(1).getBirthDate()).isBeforeOrEqualTo(list.get(2).getBirthDate());
    }

    @Test
    public void testOrderByDesc() {
        // GIVEN

        // WHEN
        List<Person> list = fluent.select(Person.class).orderBy("birthDate", Order.DESC).limit(3).fetch();

        //THEN
        assertThat(list.get(0).getBirthDate()).isAfterOrEqualTo(list.get(1).getBirthDate());
        assertThat(list.get(1).getBirthDate()).isAfterOrEqualTo(list.get(2).getBirthDate());
    }

    @Test
    public void testWhere() {
        // GIVEN
        String id = "id1";

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.equals("id", id)).fetch();

        //THEN
        Assertions.assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(id);
    }
}
