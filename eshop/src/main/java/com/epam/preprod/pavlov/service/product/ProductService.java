package com.epam.preprod.pavlov.service.product;

import com.epam.preprod.pavlov.entity.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Pavlov Aleksandr
 * <p>
 * This interface provides high-level points of interacting with underlying dao objects.
 * It represents service layer in MVC pattern.
 */
public interface ProductService {
    /**
     * This method extract all products from repository. By particular offset and limit.
     *
     * @param offset offset parameter
     * @param limit  limit parameter
     * @return list of products
     */
    List<Product> getAllProducts(int offset, int limit);

    /**
     * This method extracts all products based on url query that contains inside request object.
     *
     * @param request request that will be used to obtaining url query
     * @param offset  particular offset
     * @param limit   limit parameter
     * @return list of products
     */
    List<Product> getPreferredProducts(HttpServletRequest request, int offset, int limit);


    /**
     * This method calculates number of products by specified url query that based inside request object
     *
     * @param request request that will be used to obtaining url query
     * @return number of products
     */
    int getProductCount(HttpServletRequest request);

    int getProductCount();

    Product getProductById(int id);

}
