package at.lemme.orm.fluent.api.annotation;

import java.lang.annotation.*;

/**
 * Created by thomas18 on 31.01.2017.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();
}
