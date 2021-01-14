package com.epam.preprod.pavlov.dao.impl;

import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.entity.order.BriefOrderUserInfo;
import com.epam.preprod.pavlov.entity.order.Order;
import com.epam.preprod.pavlov.entity.order.OrderDetail;
import com.epam.preprod.pavlov.entity.order.OrderPaymentType;
import com.epam.preprod.pavlov.entity.order.OrderStatus;
import com.epam.preprod.pavlov.util.ThreadLocalConnectionStorage;
import org.apache.commons.dbutils.QueryRunner;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderDaoImplTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @Mock
    private QueryRunner queryRunner;
    @InjectMocks
    private OrderDaoImpl orderDaoImpl;
    private Order order;
    private Map<Product,Integer> productMap;

    @Before
    public void init() {
        User user = new User();
        user.setId(1);
        user.setName("name");
        user.setSurname("surname");

        order = new Order();
        order.setUserInfo(new BriefOrderUserInfo(user));
        order.setPaymentType(new OrderPaymentType(1, "payment"));
        order.setOrderStatus(new OrderStatus(1, "status"));
        order.setOrderDetail(new OrderDetail(1, "detail"));
        productMap = new HashMap<>();
        Product product = new Product();
        product.setProductId(1);
        Product product2 = new Product();
        product2.setProductId(2);
        productMap.put(product, 1);
        productMap.put(product2, 2);
    }

    @Test
    public void addOrder() throws SQLException {
        ThreadLocalConnectionStorage.setConnection(connection);
        when(connection.prepareStatement(OrderDaoImpl.INSERT_ORDER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(1, order.getOrderStatus().getOrderStatusId());
        doNothing().when(preparedStatement).setInt(2, order.getUserInfo().getUserId());
        doNothing().when(preparedStatement).setInt(3, order.getOrderDetail().getOrderDetailId());
        doNothing().when(preparedStatement).setInt(4, order.getPaymentType().getOrderPaymentTypeId());
        when(preparedStatement.executeUpdate()).thenReturn(0);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        int key = orderDaoImpl.addOrder(order);
        assertEquals(1, key);
        verify(preparedStatement).setInt(1, order.getOrderStatus().getOrderStatusId());
        verify(preparedStatement).setInt(2, order.getUserInfo().getUserId());
        verify(preparedStatement).setInt(3, order.getOrderDetail().getOrderDetailId());
        verify(preparedStatement).setInt(4, order.getPaymentType().getOrderPaymentTypeId());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void addOrderProductList() throws SQLException {
        ThreadLocalConnectionStorage.setConnection(connection);
        when(queryRunner.update(connection,OrderDaoImpl.INSERT_ORDER_PRODUCTS, 1,1,1)).thenReturn(1);
        when(queryRunner.update(connection,OrderDaoImpl.INSERT_ORDER_PRODUCTS, 1,2,2)).thenReturn(1);

        orderDaoImpl.addOrderProductList(1,productMap);

        verify(queryRunner).update(connection,OrderDaoImpl.INSERT_ORDER_PRODUCTS, 1,1,1);
        verify(queryRunner).update(connection,OrderDaoImpl.INSERT_ORDER_PRODUCTS, 1,2,2);
    }
}