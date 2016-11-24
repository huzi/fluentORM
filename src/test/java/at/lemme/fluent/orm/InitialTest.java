package at.lemme.fluent.orm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 11.11.16.
 */
public class InitialTest {

    private Connection connection;

    @Before
    public void before() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.
                getConnection("jdbc:h2:./test", "sa", "");
        clearDatabase();
    }

    @After
    public void tearDown() throws SQLException {
        clearDatabase();
        connection.close();
    }

    @Test
    public void testTest() {
        System.out.println("lala");
    }

    @Test
    public void testJdbcConnection() throws Exception {
        // WHEN
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.
                getConnection("jdbc:h2:./test", "sa", "");
        conn.close();

        //THEN - no Exception

    }

    @Test
    public void testExecuteScript() throws Exception {
        // GIVEN

        // WHEN
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("testdata.sql"));

        //THEN
        ResultSet rs = connection.prepareStatement("SELECT * FROM Person").executeQuery();
        assertThat(rs.next()).isTrue();
    }

    public void clearDatabase() throws SQLException {
        connection.prepareStatement("DROP ALL OBJECTS").execute();
    }
}
