package at.lemme.orm.fluent.api;

import java.util.List;

/**
 * Created by thomas18 on 17.02.2017.
 */
public interface Project {

    Project from(Class<?> clazz);

    Project where(Condition condition);

    Project orderBy(String attribute, Order order);

    Project limit(int limit);

    Project limit(int limit, int offset);

    <T> T mapResult(ResultMapper<T> resultMapper);

    <T> List<T> mapResults(ResultMapper<T> resultMapper);

    <T> List<T> list(Class<T> T);

}
