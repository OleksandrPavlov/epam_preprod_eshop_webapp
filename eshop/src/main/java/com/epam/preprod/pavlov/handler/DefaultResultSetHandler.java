package com.epam.preprod.pavlov.handler;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler<Object> {

    private boolean isCollection;
    private DefaultRowResultSetHandler rowHandler;

    public DefaultResultSetHandler(Class classEntity, boolean isCollection) {
        this.rowHandler = new DefaultRowResultSetHandler(classEntity);
        this.isCollection = isCollection;
    }

    public Object handle(ResultSet rs) throws SQLException {
        if (isCollection) {
            List<Object> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rowHandler.handle(rs));
            }
            return list;
        } else {
            if (rs.next()) {
                return rowHandler.handle(rs);
            } else {
                return null;
            }
        }
    }
}

