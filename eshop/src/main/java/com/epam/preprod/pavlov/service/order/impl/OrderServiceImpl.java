package com.epam.preprod.pavlov.service.order.impl;

import com.epam.preprod.pavlov.dao.OrderDao;
import com.epam.preprod.pavlov.entity.order.Order;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.service.order.OrderService;

public class OrderServiceImpl implements OrderService {
    private TransactionManager transactionManager;
    private OrderDao orderDao;

    public OrderServiceImpl(TransactionManager transactionManager, OrderDao orderDao) {
        this.transactionManager = transactionManager;
        this.orderDao = orderDao;
    }

    @Override
    public void makeOrder(Order order) {
        transactionManager.executeInTransaction(() -> {
            int currentOrderId = orderDao.addOrder(order);
            orderDao.addOrderProductList(currentOrderId, order.getProductMap());
            return true;
        });
    }
}
