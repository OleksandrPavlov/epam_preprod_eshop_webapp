package com.epam.preprod.pavlov.service.product.impl;

import com.epam.preprod.pavlov.constant.frontend.ProductPreferencesFieldNames;
import com.epam.preprod.pavlov.dao.ProductDao;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.filter.chain.FilterChain;
import com.epam.preprod.pavlov.filter.link.impl.MySQlSelectConditionTranslator;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.service.product.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements ProductService {
    public static final Comparator<Product> PRODUCT_NAME_COMPARATOR = Comparator.comparing(Product::getProductName);
    public static final Comparator<Product> PRODUCT_PRICE_COMPARATOR = Comparator.comparingDouble(Product::getPrice);

    private FilterChain<MySQlSelectConditionTranslator, String> filterChain;
    private TransactionManager transactionManager;
    private ProductDao productDao;
    private Map<String, Comparator<Product>> sortStrategies;


    public ProductServiceImpl(TransactionManager transactionManager, ProductDao productDao, FilterChain<MySQlSelectConditionTranslator, String> filterChain) {
        this.transactionManager = transactionManager;
        this.productDao = productDao;
        this.filterChain = filterChain;
        sortStrategies = new HashMap<>();
        sortStrategies.put(ProductPreferencesFieldNames.SORT_BY_NAME_OPTION, PRODUCT_NAME_COMPARATOR);
        sortStrategies.put(ProductPreferencesFieldNames.SORT_BY_PRICE_OPTION, PRODUCT_PRICE_COMPARATOR);
    }


    @Override
    public List<Product> getAllProducts(int offset, int limit) {
        return transactionManager.executeInTransaction(() -> {
            return productDao.getAllProducts(null, offset, limit);
        });
    }


    @Override
    public List<Product> getPreferredProducts(HttpServletRequest request, int offset, int limit) {
        return transactionManager.executeInTransaction(() -> productDao.getAllProducts(filterChain.doChain(request), offset, limit));
    }

    @Override
    public int getProductCount(HttpServletRequest request) {
        return transactionManager.executeInTransaction(() -> productDao.getProductCount(filterChain.doChain(request)));
    }

    @Override
    public Product getProductById(int id) {
        return transactionManager.executeInTransaction(() -> productDao.getProductById(id));
    }

    @Override
    public int getProductCount() {
        return transactionManager.executeInTransaction(() -> productDao.getProductCount(null));
    }
}
