package at.lemme.orm.fluent.impl.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by thomas18 on 13.02.2017.
 */
public class JdbcUtil {

    public static void printRow(ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        StringBuilder sb = new StringBuilder();
        sb.append('|');
        for (int i = 1; i <= columnCount; i++) {
            sb.append(resultSet.getString(i)).append('|');
        }
        System.out.println(sb.toString());
    }


    public static void printColumns(ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        StringBuilder sb = new StringBuilder();
        sb.append('|');
        for (int i = 1; i <= columnCount; i++) {
            sb.append(metaData.getColumnLabel(i)).append('|');
        }
        System.out.println(sb.toString());
    }

}
