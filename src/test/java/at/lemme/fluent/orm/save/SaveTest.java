package at.lemme.fluent.orm.save;

import at.lemme.fluent.orm.FluentOrm;
import at.lemme.fluent.orm.condition.EqualCondition;
import at.lemme.fluent.orm.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.Month.DECEMBER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 11.11.16.
 */
public class SaveTest {


    private Connection connection;
    private FluentOrm fluentOrm;

    @Before
    public void before() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:./test", "sa", "");
        connection.prepareStatement("DROP ALL OBJECTS").execute();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("testdata.sql"));
        fluentOrm = new FluentOrm(connection);
    }

    @After
    public void tearDown() throws SQLException {

        connection.prepareStatement("DROP ALL OBJECTS").execute();
        connection.close();
    }

    @Test
    public void shouldSaveSimpleObject() throws Exception {
        // GIVEN
        connection.prepareStatement("TRUNCATE TABLE Person").execute();
        Person person = new Person(null, "Anja", "Lang", LocalDate.now());

        // WHEN
        fluentOrm.save().byObject(person).execute();

        //THEN
        List<Person> persons = fluentOrm.select().from(Person.class).list();
        Optional<Person> personOptional = persons.stream()
                .filter(p -> "Anja".equals(p.getFirstName()))
                .filter(p -> "Lang".equals(p.getLastName())).findFirst();
        assertThat(personOptional.isPresent()).isTrue();
    }

    @Test
    public void shouldSaveMultipleSimpleObjects() throws Exception {
        // GIVEN
        connection.prepareStatement("TRUNCATE TABLE Person").execute();
        Person person1 = new Person(null, "Anja", "Lang", LocalDate.now());
        Person person2 = new Person(null, "Susi", "Lang", LocalDate.now().minusYears(30));

        // WHEN
        fluentOrm.save().byObject(person1).execute();
        fluentOrm.save().byObject(person2).execute();

        //THEN
        List<Person> persons = fluentOrm.select().from(Person.class).list();
        List<Person> personList = persons.stream()
                .filter(p -> "Lang".equals(p.getLastName())).collect(Collectors.toList());
        assertThat(personList).hasSize(2);
    }

    @Test
    public void testShouldSaveLocalDate() {
        // GIVEN
        Person p = new Person(null, "Thomas", "Lemmé", LocalDate.of(1984, DECEMBER, 12));

        // WHEN
        p = fluentOrm.save().byObject(p).execute();

        //THEN
        Person saved = fluentOrm.select()
                .from(Person.class).where(EqualCondition.create("id", p.getId()))
                .list().get(0);

        assertThat(saved.getBirthDate()).isEqualTo(LocalDate.of(1984, DECEMBER, 12));

    }

}
