package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.FormParameters;
import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.entity.RegistrationForm;
import com.epam.preprod.pavlov.service.account.AccountService;
import com.epam.preprod.pavlov.util.model.NotificationContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

import static com.epam.preprod.pavlov.constant.RequestConstants.RESOURCE_BUNDLE;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class RegistrationServletTest {
    private static final String VALID_NAME = "Name";
    private static final String VALID_SURNAME = "Surname";
    private static final String VALID_EMAIL = "email@email.email";
    private static final String VALID_PASSWORD = "password";
    private static final String VALID_CONFIRMED_PASSWORD = "password";
    private static final String VALID_LOGIN = "login";
    private static final String CONTEXT = "/context";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private Part avatarPart;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private RegistrationServlet registrationServlet;

    @Test
    public void shouldMoveFormFromSessionToRequestWhenDoGetMethodInvoked() throws ServletException, IOException {
        RegistrationForm form = new RegistrationForm();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.REGISTRATION_FORM)).thenReturn(form);
        when(request.getRequestDispatcher(PathConstants.ENTER_FRAME)).thenReturn(dispatcher);

        registrationServlet.doGet(request, response);

        verify(request).setAttribute(RequestConstants.REGISTRATION_FORM, form);
        verify(session).removeAttribute(RequestConstants.REGISTRATION_FORM);
    }

    @Test
    public void shouldRequestCleaningNotificationContextWhenDoGetMethodInvoked() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(PathConstants.ENTER_FRAME)).thenReturn(dispatcher);
        registrationServlet.doGet(request, response);
        verify(session).setAttribute(RequestConstants.CLEAN_ERROR_CONTEXT_FLAG, true);
    }

    @Test
    public void shouldSendRedirectToCaptchaServletWithEmptyErrorContainerWhenDoPostWithCorrectlyFormFillingWasCalled() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        expectValidRegistrationForm();
        when(avatarPart.getContentType()).thenReturn("application/octet-stream");
        when(request.getAttribute(RESOURCE_BUNDLE)).thenReturn(null);
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        when(accountService.getByLogin("login")).thenReturn(null);
        when(request.getContextPath()).thenReturn(CONTEXT);
        registrationServlet.doPost(request, response);
        verify(response).sendRedirect(request.getContextPath() + PathConstants.CAPTCHA_SERVLET);
    }

    private void expectValidRegistrationForm() throws IOException, ServletException {
        when(request.getParameter(FormParameters.NAME_FIELD)).thenReturn(VALID_NAME);
        when(request.getParameter(FormParameters.SURNAME_FIELD)).thenReturn(VALID_SURNAME);
        when(request.getParameter(FormParameters.EMAIL_FIELD)).thenReturn(VALID_EMAIL);
        when(request.getParameter(FormParameters.PASSWORD_FIELD)).thenReturn(VALID_PASSWORD);
        when(request.getParameter(FormParameters.NOTIFICATION_CHECKBOX)).thenReturn(null);
        when(request.getParameter(FormParameters.PASSWORD_CONFIRM)).thenReturn(VALID_CONFIRMED_PASSWORD);
        when(request.getPart(FormParameters.AVATAR_FILE)).thenReturn(avatarPart);
        when(request.getParameter(FormParameters.LOGIN_FIELD)).thenReturn(VALID_LOGIN);
    }

}