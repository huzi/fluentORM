package at.lemme.fluent.orm;

import at.lemme.orm.fluent.F;
import at.lemme.orm.fluent.api.Fluent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by thomas on 16.11.16.
 */
abstract public class BaseDbTest {

    protected static Connection connection;
    protected static Fluent fluent;

    @BeforeClass
    public static void beforeClass() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:./test", "sa", "");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fluentOrmTest", "root", "root");
        connection.prepareStatement("DROP TABLE IF EXISTS Person").execute();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("testdata.sql"));
        fluent = new F(connection);
        connection.setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException {
        connection.rollback();
    }

    @AfterClass
    public static void afterClass() throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS Person").execute();
        connection.commit();
        connection.close();
    }
}
