package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.FormParameters;
import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.entity.LoginForm;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.exception.AuthorizationException;
import com.epam.preprod.pavlov.service.account.AccountService;
import com.epam.preprod.pavlov.service.additional.AvatarService;
import com.epam.preprod.pavlov.util.model.NotificationContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

import static com.epam.preprod.pavlov.constant.RequestConstants.LOGIN_CALLER;
import static com.epam.preprod.pavlov.constant.RequestConstants.LOGIN_FRAME_CALLER;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServletTest {
    private static final String CONTEXT = "/context";
    private static final User USER_1 = new User();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private AccountService accountService;
    @Mock
    private AvatarService avatarService;
    @InjectMocks
    private LoginServlet loginServlet;

    @Before
    public void before() {
        USER_1.setPassword("password");
    }

    @Test
    public void shouldLoginUserWithAcceptableCredentialsWhenDoPostMethodCalled() throws AuthorizationException, IOException, ServletException {
        LoginForm loginForm = new LoginForm();
        loginForm.setLogin("login");
        loginForm.setPassword("password");
        loginForm.setRemember(true);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(FormParameters.LOGIN_FIELD)).thenReturn("login");
        when(request.getParameter(FormParameters.PASSWORD_FIELD)).thenReturn("password");
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        when(request.getParameter(FormParameters.REMEMBER_ME)).thenReturn("on");
        when(accountService.login(loginForm)).thenReturn(USER_1);
        when(avatarService.getPathToAvatar(USER_1.getAvatar())).thenReturn("path");
        when(request.getContextPath()).thenReturn(CONTEXT);

        loginServlet.doPost(request, response);
        Mockito.verify(session).setAttribute(SessionConstants.CURRENT_USER, USER_1);
    Boolean b1 =new Boolean(true);
    }
}
