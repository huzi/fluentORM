package at.lemme.orm.fluent.impl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by thomas18 on 13.02.2017.
 */
public class JdbcUtil {

    private static Logger log = LoggerFactory.getLogger(JdbcUtil.class);

    public static void printRow(ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();


        StringBuilder sb = new StringBuilder();
        sb.append('|');
        for (int i = 1; i <= columnCount; i++) {
            String value = resultSet.getString(i);
            value = String.format("%1$" + metaData.getColumnLabel(i).length() + "s", value);
            sb.append(value).append('|');
        }
        log.debug(sb.toString());
    }


    public static void printColumns(ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        StringBuilder sb = new StringBuilder();
        sb.append('|');
        for (int i = 1; i <= columnCount; i++) {
            sb.append(metaData.getColumnLabel(i)).append('|');
        }
        log.debug(sb.toString());
    }

}
