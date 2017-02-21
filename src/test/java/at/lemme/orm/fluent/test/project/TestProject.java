package at.lemme.orm.fluent.test.project;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Expressions;
import at.lemme.orm.fluent.api.ResultMapper;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by thomas18 on 20.02.2017.
 */
public class TestProject extends BaseDbTest {

    ResultMapper<Person> resultMapper = (rs, rowNum) -> {
        return null;
    };

    final ResultMapper<String> stringResultMapper = (rs, rowNum) -> {
        try {
            return rs.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    @Test
    @Ignore
    public void test() {
        // WHEN

        List<String> list = fluent.select(Expressions.column("firstName")).from(Person.class).mapResults(stringResultMapper);

        //THEN
        Assertions.assertThat(list).contains("Thomas");
    }
}
