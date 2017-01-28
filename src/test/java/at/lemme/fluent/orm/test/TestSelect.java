package at.lemme.fluent.orm.test;

import at.lemme.fluent.orm.BaseDbTest;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 22.01.17.
 */
public class TestSelect extends BaseDbTest {

    @Test
    public void testSelectPerson() throws SQLException {
        // GIVEN
        LocalDateTime now = LocalDateTime.now();
        Person p = new Person("1", "Thomas", "Lemmé", LocalDate.of(1984, 12, 18), now, 1);

        // WHEN
        List<Person> list = fluent.select(Person.class).fetch();

        //THEN
        assertThat(list).isNotEmpty();
    }

    @Test
    public void testLimit() {
        // GIVEN

        // WHEN
        List<Person> list = fluent.select(Person.class).limit(3).fetch();

        //THEN
        assertThat(list).hasSize(3);

    }

}
