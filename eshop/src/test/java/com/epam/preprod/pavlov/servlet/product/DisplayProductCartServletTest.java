package com.epam.preprod.pavlov.servlet.product;

import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.servlet.AbstractServlet;
import com.epam.preprod.pavlov.util.ProductCartUtil;
import com.epam.preprod.pavlov.util.model.NotificationContainer;
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
import java.util.HashMap;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DisplayProductCartServletTest {
    private static final Cookie VALID_COOKIE = new Cookie(ProductCartUtil.CART_COOKIE_NAME, "{}");

    @Mock
    HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    RequestDispatcher requestDispatcher;
    @InjectMocks
    DisplayProductCartServlet displayProductCartServlet;

    @Test
    public void shouldGetCartFromCookieAndRedirectOnOrderPage() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        when(request.getCookies()).thenReturn(new Cookie[]{VALID_COOKIE});
        when(request.getRequestDispatcher(PathConstants.CART_FRAME)).thenReturn(requestDispatcher);

        displayProductCartServlet.doGet(request, response);

        verify(request).getCookies();
    }
}