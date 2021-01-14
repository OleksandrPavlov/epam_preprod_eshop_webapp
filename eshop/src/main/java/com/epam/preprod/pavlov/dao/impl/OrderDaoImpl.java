package com.epam.preprod.pavlov.dao.impl;

import com.epam.preprod.pavlov.constant.ErrorMessages;
import com.epam.preprod.pavlov.constant.ErrorTitles;
import com.epam.preprod.pavlov.dao.OrderDao;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.order.Order;
import com.epam.preprod.pavlov.exception.ApplicationException;
import com.epam.preprod.pavlov.util.ThreadLocalConnectionStorage;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Map;

public class OrderDaoImpl implements OrderDao {
    private static final int ORDER_ID = 1;
    private static final int USER_ID = 2;
    private static final int DETAIL_ID = 3;
    private static final int PAYMENT_TYPE_ID = 4;
    private static final Logger ORDER_DAO_LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class);
    public static final String INSERT_ORDER_QUERY = "INSERT INTO Orders(Order_Status_ID, Order_Date, User_ID, Order_Detail_ID, Order_Payment_Type_ID) VALUES(?, now(), ?, ?, ?);";
    public static final String INSERT_ORDER_PRODUCTS = "INSERT INTO Order_Product_Sets(Order_ID,Product_ID,Product_Count) VALUES(?,?,?);";
    private QueryRunner queryRunner;

    public OrderDaoImpl() {
        this.queryRunner = new QueryRunner();
    }

    @Override
    public int addOrder(Order order) {

        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = ThreadLocalConnectionStorage.getConnection();
            statement = connection.prepareStatement(INSERT_ORDER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(ORDER_ID, order.getOrderStatus().getOrderStatusId());
            statement.setInt(USER_ID, order.getUserInfo().getUserId());
            statement.setInt(DETAIL_ID, order.getOrderDetail().getOrderDetailId());
            statement.setInt(PAYMENT_TYPE_ID, order.getPaymentType().getOrderPaymentTypeId());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            sqlExceptionHandler("Sql exception occurred during order inserting!", connection);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    ORDER_DAO_LOGGER.info("Fail closing resultSet object!");
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    ORDER_DAO_LOGGER.info("Fail closing PrepareStatement object!");
                }
            }
        }
        return 0;
    }

    @Override
    public void addOrderProductList(int orderId, Map<Product, Integer> productMap) {
        Connection connection = ThreadLocalConnectionStorage.getConnection();
        productMap.forEach((key, value) -> {
            try {
                queryRunner.update(connection, INSERT_ORDER_PRODUCTS,
                        orderId,
                        key.getProductId(),
                        value);
            } catch (SQLException e) {
                sqlExceptionHandler("Sql exception occurred during product set inserting!", connection);
            }
        });
    }

    private void sqlExceptionHandler(String sqlErrorMessage, Connection connection) throws ApplicationException {
        try {
            connection.setClientInfo(ErrorTitles.SQL_EXCEPTION, ErrorMessages.SQL_EXCEPTION_BODY);
        } catch (SQLClientInfoException e) {
            ORDER_DAO_LOGGER.error(ErrorMessages.CLIENT_INFO_ERROR_MESSAGE);
            throw new ApplicationException(sqlErrorMessage);
        }
        ORDER_DAO_LOGGER.error(sqlErrorMessage);
        throw new ApplicationException(sqlErrorMessage);
    }
}
