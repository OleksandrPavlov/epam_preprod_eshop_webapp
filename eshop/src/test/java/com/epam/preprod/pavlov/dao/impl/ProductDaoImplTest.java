package com.epam.preprod.pavlov.dao.impl;

import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.exception.ApplicationException;
import com.epam.preprod.pavlov.handler.DefaultResultSetHandler;
import com.epam.preprod.pavlov.util.ThreadLocalConnectionStorage;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDaoImplTest {
    private static final String QUERY = "some query";
    @Mock
    private PreparedStatement statement;
    @Mock
    private DefaultResultSetHandler resultSetHandler;
    @Mock
    private Connection connection;
    @Mock
    private QueryRunner runner;
    @InjectMocks
    private ProductDaoImpl productDao;

    @Before
    public void init() {
        ThreadLocalConnectionStorage.setConnection(connection);
    }

    @After
    public void destroy() {
        ThreadLocalConnectionStorage.removeConnection();
    }

    @Test
    public void shouldReturnProductListWhenGetAllProductsCalled() throws SQLException {
        when(runner.query(connection, ProductDaoImpl.SELECT_ALL_PRODUCTS_QUERY + ProductDaoImpl.OFFSET_LIMIT_PART + ProductDaoImpl.SQL_DELIMITER, resultSetHandler, 1, 1)).thenReturn(new ArrayList<Product>());
        productDao.getAllProducts(null, 1, 1);
        verify(runner).query(connection, ProductDaoImpl.SELECT_ALL_PRODUCTS_QUERY + ProductDaoImpl.OFFSET_LIMIT_PART + ProductDaoImpl.SQL_DELIMITER, resultSetHandler, 1, 1);
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowApplicationExceptionWhenGetAllProductsCalled() throws SQLException {
        when(runner.query(connection, ProductDaoImpl.SELECT_ALL_PRODUCTS_QUERY + ProductDaoImpl.OFFSET_LIMIT_PART + ProductDaoImpl.SQL_DELIMITER, resultSetHandler, 1, 1)).thenThrow(new SQLException());
        productDao.getAllProducts(null, 1, 1);
        verify(runner).query(connection, ProductDaoImpl.SELECT_ALL_PRODUCTS_QUERY + ProductDaoImpl.OFFSET_LIMIT_PART + ProductDaoImpl.SQL_DELIMITER, resultSetHandler, 1, 1);
    }

    @Test
    public void shouldReturnProductCount() throws SQLException {
        ResultSet result = mock(ResultSet.class);
        when(connection.prepareStatement(ProductDaoImpl.COUNT_ALL_PRODUCTS + QUERY + ProductDaoImpl.SQL_DELIMITER)).thenReturn(statement);
        when(statement.execute()).thenReturn(true);
        when(statement.getResultSet()).thenReturn(result);
        when(result.next()).thenReturn(true);
        when(result.getInt(1)).thenReturn(1);
        int actualNumber = productDao.getProductCount(QUERY);
        assertEquals(actualNumber, 1);
        verify(result).getInt(1);
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowApplicationExceptionWhenGetProductCountCalled() throws SQLException {
        ResultSet result = mock(ResultSet.class);
        when(connection.prepareStatement(ProductDaoImpl.COUNT_ALL_PRODUCTS + QUERY + ProductDaoImpl.SQL_DELIMITER)).thenReturn(statement);
        when(statement.execute()).thenThrow(new SQLException());
        productDao.getProductCount(QUERY);
        verify(statement).execute();
    }

    @Test
    public void shouldReturnProductListWhenGetProductsByQueryCalled() throws SQLException {
        when(runner.query(connection, ProductDaoImpl.SELECT_ALL_PRODUCTS_QUERY + QUERY + ProductDaoImpl.OFFSET_LIMIT_PART + ProductDaoImpl.SQL_DELIMITER, resultSetHandler, 1, 1)).thenReturn(new ArrayList<Product>());
        productDao.getAllProducts(QUERY, 1, 1);
        verify(runner).query(connection, ProductDaoImpl.SELECT_ALL_PRODUCTS_QUERY+QUERY + ProductDaoImpl.OFFSET_LIMIT_PART + ProductDaoImpl.SQL_DELIMITER, resultSetHandler, 1, 1);
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowApplicationExceptionWhenGetProductsByQueryCalled() throws SQLException {
        when(runner.query(connection, ProductDaoImpl.SELECT_ALL_PRODUCTS_QUERY+QUERY + ProductDaoImpl.OFFSET_LIMIT_PART + ProductDaoImpl.SQL_DELIMITER, resultSetHandler, 1, 1)).thenThrow(new SQLException());
        productDao.getAllProducts(QUERY, 1, 1);
        verify(runner).query(connection, ProductDaoImpl.SELECT_ALL_PRODUCTS_QUERY + QUERY + ProductDaoImpl.OFFSET_LIMIT_PART + ProductDaoImpl.SQL_DELIMITER, resultSetHandler, 1, 1);
    }
}