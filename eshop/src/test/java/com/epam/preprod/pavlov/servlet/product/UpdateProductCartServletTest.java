package com.epam.preprod.pavlov.servlet.product;

import com.epam.preprod.pavlov.constant.product.ProductParameters;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.cart.ajax.UpdateCartRequest;
import com.epam.preprod.pavlov.service.product.ProductService;
import com.epam.preprod.pavlov.util.ProductCartUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateProductCartServletTest {
    private static final Cookie VALID_COOKIE = new Cookie(ProductCartUtil.CART_COOKIE_NAME, "{\"{\\\"productId\\\":1,\\\"price\\\":171.5,\\\"productName\\\":\\\"Lopata\\\"}\":1}");
    @Mock
    HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ProductService productService;
    @Mock
    RequestDispatcher requestDispatcher;
    @Mock
    PrintWriter printWriter;
    @InjectMocks
    UpdateProductCartServlet updateServlet;
    private Product product;

    @Before
    public void init() {
        product = new Product();
        product.setProductId(1);
        product.setProductName("Lopata");
        product.setPrice(171.5);
    }

    @Test
    public void shouldPutUpdatedProductCartToCookieWhenDoPostMethodCalled() throws ServletException, IOException {
        when(request.getParameter(ProductParameters.PRODUCT_ID)).thenReturn("1");
        when(request.getParameter(ProductParameters.CART_OPERATION)).thenReturn(UpdateCartRequest.UPDATE_OPERATION);
        when(request.getParameter(ProductParameters.PRODUCT_COUNT)).thenReturn("1");
        when(request.getCookies()).thenReturn(new Cookie[]{VALID_COOKIE});
        when(productService.getProductById(1)).thenReturn(product);
        when(response.getWriter()).thenReturn(printWriter);
        doNothing().when(response).addCookie(any(Cookie.class));

        updateServlet.doPost(request, response);

        verify(response).addCookie(any(Cookie.class));
    }
}