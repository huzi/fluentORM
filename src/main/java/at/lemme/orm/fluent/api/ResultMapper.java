package at.lemme.orm.fluent.api;

import java.sql.ResultSet;

/**
 * Created by thomas18 on 17.02.2017.
 */
public interface ResultMapper<T> {

    T mapResult(ResultSet rs, int rowNum);
}
