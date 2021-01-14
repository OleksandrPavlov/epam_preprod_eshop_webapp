package com.epam.preprod.pavlov.dao.impl;

import com.epam.preprod.pavlov.constant.ErrorMessages;
import com.epam.preprod.pavlov.constant.ErrorTitles;
import com.epam.preprod.pavlov.dao.ProductDao;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.exception.ApplicationException;
import com.epam.preprod.pavlov.handler.DefaultResultSetHandler;
import com.epam.preprod.pavlov.util.ThreadLocalConnectionStorage;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ProductDaoImpl implements ProductDao {
    private static final String SELECT_ALL_PRODUCTS_BY_QUERY_EXCEPTION_MESSAGE = "Sql exception has been happened during product selection by query executing to database";
    private static final String SELECT_COUNT_BY_QUERY_PRODUCTS_EXCEPTION_MESSAGE = "Sql exception has been happened during count selection by query to database";
    private static final String SELECT_PRODUCT_BY_ID_EXCEPTION_MESSAGE = "Sql exception has been happened during selection product by id to database";

    private static final Logger PRODUCT_DAO_LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);
    public static final String SQL_DELIMITER = ";";
    public static final String SELECT_ALL_PRODUCTS_QUERY = " SELECT " +
            "pr.Product_ID, pr.Product_Name, pr_cat.Category_Name, pr.Product_Price, pr.Product_Count, pr_prod.Producer_Name,pr_des.Description_Body  " +
            "FROM Products as pr " +
            "INNER JOIN Product_Categories as pr_cat ON pr.Category_ID=pr_cat.Category_ID " +
            "INNER JOIN Product_Descriptions as pr_des ON pr.Product_ID=pr_des.Product_ID " +
            "INNER JOIN Product_Producers as pr_prod ON pr.Producer_ID=pr_prod.Producer_ID ";
    public static final String COUNT_ALL_PRODUCTS = " SELECT " +
            "COUNT(*) " +
            "FROM Products as pr " +
            "INNER JOIN Product_Categories as pr_cat ON pr.Category_ID=pr_cat.Category_ID " +
            "INNER JOIN Product_Descriptions as pr_des ON pr.Product_ID=pr_des.Product_ID " +
            "INNER JOIN Product_Producers as pr_prod ON pr.Producer_ID=pr_prod.Producer_ID ";
    public static final String OFFSET_LIMIT_PART = " LIMIT ? OFFSET ? ";
    public static final String SELECT_PRODUCT_BY_ID_CONDITION = "WHERE pr.Product_ID = ?";
    private DefaultResultSetHandler multiRowProductResultSetHandler = new DefaultResultSetHandler(Product.class, true);
    private QueryRunner queryRunner;

    public ProductDaoImpl() {
        queryRunner = new QueryRunner();
    }

    @Override
    public int getProductCount(String query) {
        Connection connection = ThreadLocalConnectionStorage.getConnection();
        try {
            PreparedStatement statement;
            if (Objects.nonNull(query)) {
                statement = connection.prepareStatement(String.format("%s%s;", COUNT_ALL_PRODUCTS, query));
            } else {
                statement = connection.prepareStatement(String.format("%s;", COUNT_ALL_PRODUCTS));
            }
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            sqlExceptionHandler(SELECT_COUNT_BY_QUERY_PRODUCTS_EXCEPTION_MESSAGE, connection);
        }
        return 0;
    }

    @Override
    public Product getProductById(int id) {
        Connection connection = ThreadLocalConnectionStorage.getConnection();
        Product product = null;
        try {
            product = (Product) queryRunner.query(connection, String.format("%s%s;", SELECT_ALL_PRODUCTS_QUERY, SELECT_PRODUCT_BY_ID_CONDITION), new DefaultResultSetHandler(Product.class, false), id);
        } catch (SQLException e) {
            sqlExceptionHandler(SELECT_PRODUCT_BY_ID_EXCEPTION_MESSAGE, connection);
        }
        return product;
    }

    @Override
    public List<Product> getAllProducts(String query, int offset, int limit) {
        Connection connection = ThreadLocalConnectionStorage.getConnection();
        List<Product> products = null;
        try {
            if (Objects.nonNull(query)) {
                products = (List<Product>) queryRunner.query(connection, String.format("%s%s%s;", SELECT_ALL_PRODUCTS_QUERY, query, OFFSET_LIMIT_PART), multiRowProductResultSetHandler, limit, offset);
            } else {
                products = (List<Product>) queryRunner.query(connection, String.format("%s%s;", SELECT_ALL_PRODUCTS_QUERY, OFFSET_LIMIT_PART), multiRowProductResultSetHandler, limit, offset);
            }
        } catch (SQLException e) {
            sqlExceptionHandler(SELECT_ALL_PRODUCTS_BY_QUERY_EXCEPTION_MESSAGE, connection);
        }
        return products;
    }

    private void sqlExceptionHandler(String sqlErrorMessage, Connection connection) throws ApplicationException {
        try {
            connection.setClientInfo(ErrorTitles.SQL_EXCEPTION, ErrorMessages.SQL_EXCEPTION_BODY);
        } catch (SQLClientInfoException e) {
            PRODUCT_DAO_LOGGER.error(ErrorMessages.CLIENT_INFO_ERROR_MESSAGE);
            throw new ApplicationException(sqlErrorMessage);
        }
        PRODUCT_DAO_LOGGER.error(sqlErrorMessage);
        throw new ApplicationException(sqlErrorMessage);
    }
}
