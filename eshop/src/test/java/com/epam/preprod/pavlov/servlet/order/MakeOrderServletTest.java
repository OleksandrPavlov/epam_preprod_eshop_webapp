package com.epam.preprod.pavlov.servlet.order;

import com.epam.preprod.pavlov.constant.NotificationContextConstants;
import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.constant.sql.order.OrderConstants;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.service.order.OrderService;
import com.epam.preprod.pavlov.util.ProductCartUtil;
import com.epam.preprod.pavlov.util.model.NotificationContainer;
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
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MakeOrderServletTest {
    private static final Cookie VALID_COOKIE = new Cookie(ProductCartUtil.CART_COOKIE_NAME, "{}");
    @Mock
    HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private OrderService orderService;
    @Mock
    RequestDispatcher requestDispatcher;
    @InjectMocks
    MakeOrderServlet makeOrderServlet;
    private User user;
    private Cookie[] cookies;

    @Before
    public void init() {
        user = new User();
        user.setId(1);
        user.setName("name");
        user.setSurname("surname");

        cookies = new Cookie[1];
        cookies[0] = VALID_COOKIE;
    }

    @Test
    public void shouldMakeRequestOnCleaningNotificationContextWhenDoGetMethodInvoked() throws ServletException, IOException {
        when(request.getRequestDispatcher(PathConstants.PRODUCT_LIST_FRAME)).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        makeOrderServlet.doGet(request, response);

        verify(request).getRequestDispatcher(PathConstants.PRODUCT_LIST_FRAME);
        verify(session).setAttribute(RequestConstants.CLEAN_ERROR_CONTEXT_FLAG,true);
    }

    @Test
    public void shouldMakeOrderAndRemoveCookieWhenPostMethodCalled() throws  IOException {
        when(request.getParameter(RequestConstants.ORDER_PAYMENT)).thenReturn(OrderConstants.ORDER_APPLE_PAYMENT);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        when(session.getAttribute(SessionConstants.CURRENT_USER)).thenReturn(user);
        when(request.getCookies()).thenReturn(cookies);
        makeOrderServlet.doPost(request, response);

        verify(orderService).makeOrder(any());
        verify(response).addCookie(any());
    }
}