package at.lemme.orm.fluent.test;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 22.01.17.
 */
public class TestSelectConditions extends BaseDbTest {

    @Test
    public void testWhereEqualsString() {
        // GIVEN
        String id = "id1";

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.equals("id", id)).fetch();

        //THEN
        Assertions.assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(id);
    }

    @Test
    public void testWhereEqualsDate() {
        // GIVEN
        LocalDate birthday = LocalDate.of(1984, 12, 18);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.equals("birthDate", birthday)).fetch();

        //THEN
        Assertions.assertThat(list).hasSize(1);
        assertThat(list.get(0).getBirthDate()).isEqualTo(birthday);
    }

    @Test
    public void testWhereEqualsDateTime() {
        // GIVEN
        LocalDateTime lastLogin = LocalDateTime.of(2017, 01, 22, 13, 37, 0);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.equals("lastLogin", lastLogin)).fetch();

        //THEN
        Assertions.assertThat(list).hasSize(1);
        assertThat(list.get(0).getLastLogin()).isEqualTo(lastLogin);
    }

    @Test
    public void testWhereEqualsInteger() {
        // GIVEN
        int loginCount = 100;

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.equals("loginCount", loginCount)).fetch();

        //THEN
        Assertions.assertThat(list).hasSize(1);
        assertThat(list.get(0).getLoginCount()).isEqualTo(loginCount);
    }

    @Test
    public void testWhereAndEquals() {
        // GIVEN
        String id = "id0";

        // WHEN
        List<Person> list = fluent.select(Person.class).where(Conditions.and(
                Conditions.equals("firstName", "Thomas"),
                Conditions.equals("lastName", "Lemmé")
        )).fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getId).collect(Collectors.toList())).contains(id);
    }

    @Test
    public void testWhereOrEquals() {
        // GIVEN
        String firstname = "Thomas";
        String lastname = "Lemmé";

        // WHEN
        List<Person> list = fluent.select(Person.class).where(Conditions.or(
                Conditions.equals("firstName", firstname),
                Conditions.equals("lastName", lastname)
        )).fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list)
                .allMatch(person -> person.getFirstName().equals(firstname) || person.getLastName().equals(lastname));
    }

    @Test
    public void testWhereIsNull() throws SQLException {
        // GIVEN
        String id = "id0";
        connection.createStatement().executeUpdate("UPDATE Person SET column_firstName = null WHERE column_id = '" + id + "'");

        // WHEN
        List<Person> list = fluent.select(Person.class).where(Conditions.isNull("firstName")).fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getId).collect(Collectors.toList())).contains(id);
    }

    @Test
    public void testWhereIsNotNull() throws SQLException {
        // GIVEN
        String id = "id0";
        connection.createStatement().executeUpdate("UPDATE Person SET column_lastName = null WHERE column_id = '" + id + "'");

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.and(
                        Conditions.isNotNull("lastName"),
                        Conditions.equals("firstName", "Thomas")))
                .fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getId).collect(Collectors.toList())).doesNotContain(id);
    }

    @Test
    public void testWhereBetweenDate() {
        // GIVEN
        String id = "id0";
        LocalDate start = LocalDate.of(1984, 12, 1);
        LocalDate end = LocalDate.of(1984, 12, 31);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.between("birthDate", start, end))
                .fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getId).collect(Collectors.toList())).contains(id);
    }

    @Test
    public void testWhereBetweenInteger() {
        // GIVEN
        String id = "id0";

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.between("loginCount", 99, 101))
                .fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getId).collect(Collectors.toList())).contains(id);
    }

    @Test
    public void testWhereIn() {
        // GIVEN
        String firstName1 = "Thomas";
        String firstName2 = "Anna";
        String firstName3 = "Ida";

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.in("firstName", firstName1, firstName2, firstName3))
                .fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getFirstName).collect(Collectors.toList()))
                .contains(firstName1, firstName2, firstName3);
    }

    @Test
    public void testWhereInDate() {
        // GIVEN
        LocalDate birthDate1 = LocalDate.of(1984, 12, 18);
        LocalDate birthDate2 = LocalDate.of(1968, 2, 27);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.in("birthDate", birthDate1, birthDate2))
                .fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getBirthDate).collect(Collectors.toList()))
                .contains(birthDate1, birthDate2);
    }

    @Test
    public void testWhereNotIn() {
        // GIVEN
        String firstName1 = "Thomas";
        String firstName2 = "Anna";
        String firstName3 = "Ida";

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.notIn("firstName", firstName1, firstName2, firstName3))
                .fetch();

        //THEN
        assertThat(list.stream().map(Person::getFirstName).collect(Collectors.toList()))
                .doesNotContain(firstName1, firstName2, firstName3);
    }

    @Test
    public void testWhereNotInDate() {
        // GIVEN
        LocalDate birthDate1 = LocalDate.of(1984, 12, 18);
        LocalDate birthDate2 = LocalDate.of(1968, 2, 27);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.notIn("birthDate", birthDate1, birthDate2))
                .fetch();

        //THEN
        assertThat(list.stream().map(Person::getBirthDate).collect(Collectors.toList()))
                .doesNotContain(birthDate1, birthDate2);
    }

    @Test
    public void testWhereNotEquals() {
        // GIVEN
        String firstName = "Thomas";

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.notEquals("firstName", firstName))
                .fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getFirstName).collect(Collectors.toList()))
                .doesNotContain(firstName);
    }

    @Test
    public void testWhereNotEqualsDate() {
        // GIVEN
        LocalDate birthDate = LocalDate.of(1984, 12, 18);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.notEquals("birthDate", birthDate))
                .fetch();

        //THEN
        Assertions.assertThat(list).isNotEmpty();
        assertThat(list.stream().map(Person::getBirthDate).collect(Collectors.toList()))
                .doesNotContain(birthDate);
    }

    @Test
    public void testWhereLowerThanDate() {
        // GIVEN
        LocalDate birthDate = LocalDate.of(1985, 1, 1);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.lowerThan("birthDate", birthDate))
                .fetch();

        //THEN
        assertThat(list.stream().map(Person::getBirthDate).collect(Collectors.toList()))
                .allMatch(d -> d.isBefore(birthDate));
    }

    @Test
    public void testWhereLowerThanOrEqualDate() {
        // GIVEN
        String id = "id0";
        LocalDate birthDate = LocalDate.of(1984, 12, 18);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.lowerThanOrEquals("birthDate", birthDate))
                .fetch();

        //THEN
        assertThat(list.stream().map(Person::getId).collect(Collectors.toList()))
                .contains(id);
        assertThat(list.stream().map(Person::getBirthDate).collect(Collectors.toList()))
                .allMatch(d -> d.isBefore(birthDate) || d.isEqual(birthDate));
    }


    @Test
    public void testWhereGreaterThanDate() {
        // GIVEN
        LocalDate birthDate = LocalDate.of(1984, 12, 31);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.greaterThan("birthDate", birthDate))
                .fetch();

        //THEN
        assertThat(list.stream().map(Person::getBirthDate).collect(Collectors.toList()))
                .allMatch(d -> d.isAfter(birthDate));
    }

    @Test
    public void testWhereGreaterThanOrEqualDate() {
        // GIVEN
        String id = "id0";
        LocalDate birthDate = LocalDate.of(1984, 12, 18);

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.greaterThanOrEquals("birthDate", birthDate))
                .fetch();

        //THEN
        assertThat(list.stream().map(Person::getId).collect(Collectors.toList()))
                .contains(id);
        assertThat(list.stream().map(Person::getBirthDate).collect(Collectors.toList()))
                .allMatch(d -> d.isAfter(birthDate) || d.isEqual(birthDate));
    }

    @Test
    public void testWhereLike() {
        // GIVEN
        String id = "id0";

        // WHEN
        List<Person> list = fluent.select(Person.class)
                .where(Conditions.like("lastName", "%é"))
                .fetch();

        //THEN
        assertThat(list.stream().map(Person::getId).collect(Collectors.toList()))
                .contains(id);
        assertThat(list.stream().map(Person::getLastName).collect(Collectors.toList()))
                .allMatch(name -> name.endsWith("é"));
    }
}
