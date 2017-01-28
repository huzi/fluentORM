package at.lemme.fluent.orm.test;

import at.lemme.fluent.orm.BaseDbTest;
import org.junit.Test;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 22.01.17.
 */
public class TestInsert extends BaseDbTest {

    @Test
    public void testInsertPerson() throws SQLException {
        // GIVEN
        LocalDateTime now = LocalDateTime.now();
        Person p = new Person("1", "Thomas", "Lemmé", LocalDate.of(1984, 12, 18), now, 1);

        // WHEN
        fluent.insert(p).execute();

        //THEN
        List<Person> result = fetchList("SELECT * FROM Person WHERE id='1';");
        assertThat(result).hasSize(1);
        assertThat(result).contains(p);
    }

    @Test
    public void testInsertPersons() throws SQLException {
        // GIVEN
        LocalDateTime now = LocalDateTime.now();
        Person p1 = new Person("1", "Thomas", "Lemmé", LocalDate.of(1984, 12, 18), now, 1);
        Person p2 = new Person("2", "Thomas", "Lemmé", LocalDate.of(1984, 12, 18), now, 1);
        Person p3 = new Person("3", "Thomas", "Lemmé", LocalDate.of(1984, 12, 18), now, 1);

        // WHEN
        fluent.insert(p1, p2, p3).execute();

        //THEN
        List<Person> result = fetchList("SELECT * FROM Person WHERE id IN('1', '2', '3');");
        assertThat(result).hasSize(3);
        assertThat(result).contains(p1);
        assertThat(result).contains(p2);
        assertThat(result).contains(p3);
    }

    private List<Person> fetchList(String sql) throws SQLException {
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        List<Person> result = new ArrayList<>();
        while (rs.next()) {
            Person person = new Person(rs.getString("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getDate("birthDate").toLocalDate(), rs.getTimestamp("lastLogin").toLocalDateTime(), rs.getInt("loginCount"));
            result.add(person);
        }
        return result;
    }

    @Test
    public void testInsertPreparedStatement() throws SQLException {
        // GIVEN

        // WHEN
        String insertTableSQL = "INSERT INTO Person(id, firstName, lastName, birthDate, lastLogin, loginCount) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQL);
        preparedStatement.setString(1, "x");
        preparedStatement.setString(2, "y");
        preparedStatement.setString(3, "z");
        preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
        preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setInt(6, 1);
// execute insert SQL stetement
        preparedStatement.executeUpdate();

        //THEN

    }

}
