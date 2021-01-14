package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.PathConstants;
import org.easymock.MockType;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.easymock.EasyMock.*;

public class LogoutServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private static final String CONTEXT = "/context";


    @Before
    public void init() {
        session = mock(MockType.NICE, HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void shouldInvokeAllMethodsInProperOrderWhenDoPOstMethodCalled() throws IOException, ServletException {
        expect(request.getSession()).andReturn(session);
        expect(request.getContextPath()).andReturn(CONTEXT);
        session.invalidate();

        response.sendRedirect(CONTEXT + PathConstants.PRODUCT_SERVLET);

        replay(request, response, session);

        new LogoutServlet().doPost(request, response);

        verify(request, response, session);
    }
}