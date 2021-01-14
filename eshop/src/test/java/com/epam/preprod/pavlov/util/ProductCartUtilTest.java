package com.epam.preprod.pavlov.util;

import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.cart.ProductCart;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductCartUtilTest {
    private static final Cookie VALID_COOKIE = new Cookie(ProductCartUtil.CART_COOKIE_NAME, "{\"{\\\"productId\\\":1,\\\"price\\\":171.5,\\\"productName\\\":\\\"Lopata\\\"}\":1}");
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ProductCart productCart;
    private Product product;

    @Before
    public void init() {
        product = new Product();
        product.setProductId(1);
        product.setProductName("Lopata");
        product.setPrice(171.5);
    }

    @Test
    public void shouldReturnMapOfProducts() {
        Map<Product, Integer> productMap = new HashMap<>();
        productMap.put(product, 1);

        when(request.getCookies()).thenReturn(new Cookie[]{VALID_COOKIE});

        Map<Product, Integer> resultProductMap = ProductCartUtil.getProductCartFromCookie(request);
        assertEquals(productMap, resultProductMap);
    }

    @Test
    public void shouldPutCookieJsonRepresentationToCookie() {
        Map<Product, Integer> productMap = new HashMap<>();
        productMap.put(product, 1);
        when(productCart.getProductMap()).thenReturn(productMap);
        ProductCartUtil.putProductCartToCookie(productCart, response);
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    public void shouldRemoveProductCartFromCookie() {
        doNothing().when(response).addCookie(any(Cookie.class));
        ProductCartUtil.removeCartFromCookie(response);
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    public void shouldCreateProductCartWhenFromJSONMethodCalled() {
        Map<Product, Integer> expectedProductMap = new HashMap<>();
        expectedProductMap.put(product, 1);
        JSONObject jsonObject = new JSONObject(VALID_COOKIE.getValue());
        Map<Product, Integer> productMap = ProductCartUtil.createProductCartOfJson(jsonObject);
        assertEquals(productMap, expectedProductMap);
    }

    @Test
    public void shouldReturnProductMapFromCookieWhenGetProductCartCookieCalled() {
        Cookie[] cookies = new Cookie[]{VALID_COOKIE};
        when(request.getCookies()).thenReturn(cookies);
        Cookie resultCookie = ProductCartUtil.getProductCartCookie(request);
        assertEquals(resultCookie, VALID_COOKIE);
    }

    @Test
    public void shouldReturnJavaObjectWhenProductFromJSONMethodCalled() {
        JSONObject jsonObject = new JSONObject(product);
        Product resultProduct = ProductCartUtil.productFromJSON(jsonObject);
        assertEquals(resultProduct, product);
    }

}