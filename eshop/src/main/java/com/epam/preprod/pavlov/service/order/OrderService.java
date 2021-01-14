package com.epam.preprod.pavlov.service.order;

import com.epam.preprod.pavlov.entity.order.Order;

/**
 * @version 1.0
 * This interface provides method to work with order repositories.
 * @autor Pavlov Oleksandr
 */
public interface OrderService {
    /**
     * This method create new order in repository.
     *
     * @param order the order that wll be created
     */
    void makeOrder(Order order);
}
