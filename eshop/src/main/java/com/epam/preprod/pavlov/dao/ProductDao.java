package com.epam.preprod.pavlov.dao;

import com.epam.preprod.pavlov.entity.Product;

import java.util.List;

/**
 * @author Pavlov Alesandr
 * This interface provides methods to ineracting with database.
 */
public interface ProductDao {


    /**
     * THis method provide all products by specified query and adds to query limit and offset parameters.
     *
     * @param query  that will be as request to db
     * @param offset offset parameter
     * @param limit  limit parameter
     * @return list of products
     */
    List<Product> getAllProducts(String query, int offset, int limit);

    /**
     * THis method provides quantity of products in database by specified query.
     *
     * @param query sql query
     * @return number of products
     */
    int getProductCount(String query);

    Product getProductById(int id);


}
