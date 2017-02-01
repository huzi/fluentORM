package at.lemme.orm.fluent.test.crud;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 22.01.17.
 */
public class TestUpdate extends BaseDbTest {

    @Test
    public void testUpdate() {
        //Given
        List<Person> persons = fluent.select(Person.class).where(Conditions.equals("firstName", "Thomas")).fetch();
        Assertions.assertThat(persons).isNotEmpty();

        //When
        persons.forEach(person -> person.setFirstName("Tom"));
        fluent.update(persons.toArray(new Person[persons.size()])).execute();

        //Then
        persons = fluent.select(Person.class).where(Conditions.equals("firstName", "Thomas")).fetch();
        Person person = (Person) fluent.select(Person.class).where(Conditions.equals("id", "id0")).fetch().get(0);
        Assertions.assertThat(persons).isEmpty();
        assertThat(person.getFirstName()).isEqualTo("Tom");
    }
}
