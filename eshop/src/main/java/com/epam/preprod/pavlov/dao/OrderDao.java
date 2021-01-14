package com.epam.preprod.pavlov.dao;

import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.order.Order;

import java.util.Map;

/**
 * @author Pavlov Oleksandr
 * @version 1.0
 * This interface provides methods to work with database.
 */
public interface OrderDao {
    /**
     * This method adds new order to the database and returns auto-generated key;
     *
     * @param order will be added
     * @return auto-generated key
     */
    int addOrder(Order order);

    /**
     * This method adds product map related to particular order entity in database.
     *
     * @param orderId    identifier of particular order
     * @param productMap map that will be tied
     */
    void addOrderProductList(int orderId, Map<Product, Integer> productMap);
}
