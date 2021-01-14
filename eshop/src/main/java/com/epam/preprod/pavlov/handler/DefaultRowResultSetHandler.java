package com.epam.preprod.pavlov.handler;

import com.epam.preprod.pavlov.annotation.Column;
import com.epam.preprod.pavlov.exception.ApplicationException;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DefaultRowResultSetHandler<T> implements ResultSetHandler<T> {
    private static final Logger DEFAULT_ROWRS_HANDLER = LoggerFactory.getLogger(DefaultResultSetHandler.class);
    private final Class<T> classEntity;

    DefaultRowResultSetHandler(Class<T> classEntity) {
        this.classEntity = classEntity;
    }

    public T handle(ResultSet rs) throws SQLException {
        T entity = null;
        try {
            entity = classEntity.newInstance();
            Field[] fields = classEntity.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                String columnName = null;
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null) {
                    columnName = columnAnnotation.name();
                }
                if(Objects.isNull(columnName)){
                    continue;
                }
                Object value = rs.getObject(columnName);
                field.set(entity, value);
                field.setAccessible(false);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            DEFAULT_ROWRS_HANDLER.error("Reflection exception was occurred in handle method in DefaultRowResultSetHandler");
            throw new ApplicationException(e.getMessage());
        }
        return entity;
    }
}
