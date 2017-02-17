package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.QueryParameters;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas18 on 31.01.2017.
 */
public class Parameters implements QueryParameters {
    private List<Object> params = new ArrayList<>();

    @Override
    public void add(Object o) {
        params.add(o);
    }

    @Override
    public void apply(PreparedStatement stmt) {
        int index = 1;
        try {
            for (Object value : params) {
                if (value instanceof String) {
                    stmt.setString(index, (String) value);
                } else if (value instanceof LocalDate) {
                    stmt.setDate(index, Date.valueOf((LocalDate) value));
                } else if (value instanceof LocalDateTime) {
                    stmt.setTimestamp(index, Timestamp.valueOf((LocalDateTime) value));
                } else if (value instanceof Integer) {
                    stmt.setInt(index, (int) value);
                } else {
                    throw new RuntimeException("Type not Supported!");
                }
                index++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
