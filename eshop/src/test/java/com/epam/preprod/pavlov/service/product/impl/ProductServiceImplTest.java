package com.epam.preprod.pavlov.service.product.impl;

import com.epam.preprod.pavlov.dao.ProductDao;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.filter.chain.FilterChain;
import com.epam.preprod.pavlov.filter.link.impl.MySQlSelectConditionTranslator;
import com.epam.preprod.pavlov.jdbc.Transaction;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.service.product.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    private static final String PRODUCT_NAME_A = "productA";
    private static final String QUERY = "query";
    private static final String PRODUCT_NAME_B = "productB";
    private static final double PRODUCT_PRICE_A = 23;
    private static final double PRODUCT_PRICE_B = 11;

    @Mock
    FilterChain<MySQlSelectConditionTranslator, String> filterChain;
    @Mock
    private TransactionManager transactionManager;
    @Mock
    private ProductDao productDao;
    @Mock
    private HttpServletRequest request;
    @Captor
    private ArgumentCaptor<Transaction<List<Product>>> captor;
    @Captor
    private ArgumentCaptor<Transaction<Integer>> captorInteger;

    private ProductService productService;
    private Product productA;
    private Product productB;

    @Before
    public void before() {
        productService = new ProductServiceImpl(transactionManager, productDao, filterChain);
        productA = new Product();
        productB = new Product();
        productA.setProductName(PRODUCT_NAME_A);
        productB.setProductName(PRODUCT_NAME_B);
        productB.setPrice(PRODUCT_PRICE_B);
        productA.setPrice(PRODUCT_PRICE_A);
    }

    @Test
    public void shouldReturnListOfProductsWhenGetAllProductsMethodCalled() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        when(transactionManager.executeInTransaction(captor.capture())).thenReturn(products);
        when(productDao.getAllProducts(null, 1, 1)).thenReturn(products);
        productService.getAllProducts(1, 1);
        Transaction<List<Product>> transaction = captor.getValue();
        transaction.transact();
        verify(transactionManager).executeInTransaction(captor.getValue());
        verify(productDao).getAllProducts(null, 1, 1);
    }

    @Test
    public void shouldReturnListOfProductsWhenGetAllPreferredProductsMethodCalled() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        when(transactionManager.executeInTransaction(captor.capture())).thenReturn(products);
        when(productDao.getAllProducts(QUERY, 1, 1)).thenReturn(products);
        when(filterChain.doChain(request)).thenReturn(QUERY);
        productService.getPreferredProducts(request, 1, 1);
        Transaction<List<Product>> transaction = captor.getValue();
        transaction.transact();
        verify(transactionManager).executeInTransaction(captor.getValue());
        verify(productDao).getAllProducts(QUERY, 1, 1);
    }

    @Test
    public void shouldReturnNumberOfProductsWhenGetProductCountMethodCalled() {
        when(transactionManager.executeInTransaction(captorInteger.capture())).thenReturn(1);
        when(filterChain.doChain(request)).thenReturn(QUERY);
        when(productDao.getProductCount(QUERY)).thenReturn(1);
        productService.getProductCount(request);
        Transaction<Integer> transaction = captorInteger.getValue();
        transaction.transact();

        verify(productDao).getProductCount(QUERY);
        verify(transactionManager).executeInTransaction(captorInteger.getValue());
    }
}