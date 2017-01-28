package at.lemme.fluent.orm;

import at.lemme.orm.fluent.F;
import at.lemme.orm.fluent.api.Fluent;
import org.junit.After;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by thomas on 16.11.16.
 */
abstract public class BaseDbTest {

    protected Connection connection;
    protected Fluent fluent;

    @Before
    public void before() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:./test", "sa", "");
        connection.prepareStatement("DROP ALL OBJECTS").execute();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("testdata.sql"));
        fluent = new F(connection);
    }

    @After
    public void tearDown() throws SQLException {

        connection.prepareStatement("DROP ALL OBJECTS").execute();
        connection.close();
    }
}
