package at.lemme.fluent.orm.select;

import at.lemme.fluent.orm.BaseDbTest;
import at.lemme.fluent.orm.model.Person;
import at.lemme.fluent.orm.condition.AndCondition;
import at.lemme.fluent.orm.condition.EqualCondition;
import at.lemme.fluent.orm.condition.OrCondition;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by thomas on 11.11.16.
 */
public class SelectWhereTest extends BaseDbTest {


    @Test
    public void shouldFilterList() {
        // GIVEN
        final String firstName = "Archibald";
        fluentOrm.save().byObject(new Person(null, "Archibald", "Meier", LocalDate.now()));
        fluentOrm.save().byObject(new Person(null, "Archibald", "Huber", LocalDate.now()));

        // WHEN
        List<Person> list = fluentOrm.select()
                .from(Person.class)
                .where(EqualCondition.create("firstName", firstName))
                .list();

        //THEN
        assertThat(list.stream().allMatch(p -> p.getFirstName().equals(firstName))).isTrue();
    }

    @Test
    public void shouldFilterAndCondition() {
        // GIVEN
        final String firstName = "Thomas";
        final String lastName = "Lemmé";
        fluentOrm.save().byObject(new Person(null, "Archibald", "Meier", LocalDate.now()));
        fluentOrm.save().byObject(new Person(null, "Archibald", "Huber", LocalDate.now()));

        // WHEN
        List<Person> list = fluentOrm.select()
                .from(Person.class)
                .where(AndCondition.create(
                                EqualCondition.create("firstName", firstName),
                                EqualCondition.create("lastName", lastName))
                ).list();

        //THEN
        assertThat(list.get(0).getFirstName()).isEqualTo(firstName);
        assertThat(list.get(0).getLastName()).isEqualTo(lastName);
        assertThat(list.stream().allMatch(p -> p.getFirstName().equals(firstName))).isTrue();
        assertThat(list.stream().allMatch(p -> p.getLastName().equals(lastName))).isTrue();
        System.out.println(list.get(0));
    }

    @Test
    public void shouldFilterOrCondition() {
        // GIVEN
        final String firstName = "Thomas";
        final String lastName = "Nonexisting";
        fluentOrm.save().byObject(new Person(null, "Archibald", "Meier", LocalDate.now()));

        // WHEN
        List<Person> list = fluentOrm.select()
                .from(Person.class)
                .where(OrCondition.create(
                                EqualCondition.create("firstName", firstName),
                                EqualCondition.create("lastName", lastName))
                ).list();

        //THEN
        assertThat(list.stream().allMatch(p -> {
            return p.getFirstName().equals(firstName) ||
                    p.getLastName().equals(lastName);
        })).isTrue();
    }


}
