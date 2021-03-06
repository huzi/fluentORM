package at.lemme.fluent.orm;

import at.lemme.fluent.orm.save.Save;
import at.lemme.fluent.orm.select.Select;

import java.sql.Connection;

/**
 * Created by thomas on 11.11.16.
 */
public class FluentOrm {

    final Connection connection;

    public FluentOrm(final Connection connection) {
        this.connection = connection;
    }

    public Select select(){
        return new Select(connection);
    }

    public Save save(){
        return new Save(connection);
    }
}
