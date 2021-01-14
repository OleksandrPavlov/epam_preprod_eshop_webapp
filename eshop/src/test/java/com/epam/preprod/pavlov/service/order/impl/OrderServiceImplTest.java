package com.epam.preprod.pavlov.service.order.impl;

import com.epam.preprod.pavlov.dao.OrderDao;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.order.Order;
import com.epam.preprod.pavlov.jdbc.Transaction;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.service.order.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    @Mock
    private TransactionManager transactionManager;
    @Mock
    private OrderDao orderDao;
    @Mock
    private Order order;
    @Mock
    private Map<Product, Integer> productMap;
    @Captor
    ArgumentCaptor<Transaction> captor;

    @Test
    public void makeOrder() {
        when(transactionManager.executeInTransaction(captor.capture())).thenReturn(true);
        when(orderDao.addOrder(order)).thenReturn(1);
        doNothing().when(orderDao).addOrderProductList(1, productMap);
        when(order.getProductMap()).thenReturn(productMap);
        OrderService orderService = new OrderServiceImpl(transactionManager, orderDao);
        orderService.makeOrder(order);
        Transaction transaction = captor.getValue();
        transaction.transact();

        verify(orderDao).addOrder(order);
        verify(orderDao).addOrderProductList(1, productMap);

    }
}