package at.lemme.fluent.orm.select;

import at.lemme.fluent.orm.BaseDbTest;
import at.lemme.fluent.orm.model.Person;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by thomas on 11.11.16.
 */
public class SelectTest extends BaseDbTest {


    @Test
    public void shouldReturnList() {
        // WHEN
        List<Person> list = fluentOrm.select().from(Person.class).list();

        //THEN
        assertThat(list).isNotNull();
    }

    @Test
    public void shouldReturnNonemptyList() {
        // WHEN
        List<Person> list = fluentOrm.select().from(Person.class).list();

        //THEN
        assertThat(list).isNotEmpty();
    }

    @Test
    public void shouldContainPerson() {
        // WHEN
        List<Person> list = fluentOrm.select().from(Person.class).list();

        //THEN
        Person person = list.stream().filter(p -> p.getId().equals("id1")).findFirst().get();
        assertThat(person.getFirstName()).isEqualTo("Noah");
        assertThat(person.getLastName()).isEqualTo("Schneider");
    }

    @Test
    public void shouldContainPersonWithAlias() {
        // WHEN
        List<Person> list = fluentOrm
                .select()
                .from(Person.class)
                .alias("p")
                .list();

        //THEN
        Person person = list.stream().filter(p -> p.getId().equals("id1")).findFirst().get();
        assertThat(person.getFirstName()).isEqualTo("Noah");
        assertThat(person.getLastName()).isEqualTo("Schneider");
    }
}
