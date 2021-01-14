package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.*;
import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.entity.RegistrationForm;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.exception.UserException;
import com.epam.preprod.pavlov.service.account.AccountService;
import com.epam.preprod.pavlov.service.additional.AvatarService;
import com.epam.preprod.pavlov.service.captcha.CaptchaManager;
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
import java.util.HashMap;

import static com.epam.preprod.pavlov.constant.PathConstants.*;
import static com.epam.preprod.pavlov.constant.RequestConstants.CAPTCHA_EXPIRED;
import static com.epam.preprod.pavlov.constant.RequestConstants.SAME_LOGIN;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaptchaCheckTest {

    private static final String VALID_CAPTCHA_CODE = "124567";
    private static final String INVALID_CAPTCHA_CODE = "222222";
    private static final String IMAGE_URL = "IMAGE_URL";
    private static final String CONTEXT_PATH = "/context";
    private static final CaptchaPack CAPTCHA_PACK = new CaptchaPack(VALID_CAPTCHA_CODE, IMAGE_URL);
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private CaptchaManager captchaManager;
    @Mock
    private AccountService accountService;
    @Mock
    private AvatarService avatarService;
    @InjectMocks
    private CaptchaCheckServlet captchaCheckServlet;

    @Test
    public void shouldCreateCaptchaWhenDoGetMethodWillCalled() throws IOException, ServletException {
        doNothing().when(captchaManager).recreateCaptcha(request, response, CaptchaConfigConstants.CAPTCHA_ALIVE_INTERVAL);
        when(request.getRequestDispatcher(ENTER_FRAME)).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(request, response);
        captchaCheckServlet.doGet(request, response);
        verify(captchaManager).recreateCaptcha(request, response, CaptchaConfigConstants.CAPTCHA_ALIVE_INTERVAL);
    }

    @Test
    public void shouldRedirectOnRegistrationServletWithInvalidCaptchaKeyWhenDoPostMethodWillBeCalled() throws IOException, ServletException {
        when(request.getParameter(JspConstants.USER_CAPTCHA_CODE)).thenReturn(INVALID_CAPTCHA_CODE);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        when(captchaManager.getCaptcha(request, response)).thenReturn(CAPTCHA_PACK);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        doNothing().when(response).sendRedirect(CONTEXT_PATH + REGISTRATION_SERVLET);

        captchaCheckServlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + REGISTRATION_SERVLET);
    }

    @Test
    public void shouldRedirectOnRegistrationServletDueToOverdueCaptchaWhenDoPostMethodWillBeCalled() throws IOException, ServletException {
        when(request.getParameter(JspConstants.USER_CAPTCHA_CODE)).thenReturn(VALID_CAPTCHA_CODE);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        when(captchaManager.getCaptcha(request, response)).thenReturn(null);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        doNothing().when(response).sendRedirect(CONTEXT_PATH + REGISTRATION_SERVLET);

        captchaCheckServlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + REGISTRATION_SERVLET);
    }

    @Test
    public void shouldRedirectToRegistrationFormDueToSameUserAlreadyExistWhenDoPostMethodCalled() throws IOException, ServletException, UserException {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setLogin("login");

        when(request.getParameter(JspConstants.USER_CAPTCHA_CODE)).thenReturn(VALID_CAPTCHA_CODE);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        when(session.getAttribute(SessionConstants.REGISTRATION_FORM)).thenReturn(registrationForm);
        when(captchaManager.getCaptcha(request, response)).thenReturn(CAPTCHA_PACK);
        when(accountService.getByLogin("login")).thenReturn(new User());
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        doNothing().when(response).sendRedirect(CONTEXT_PATH + REGISTRATION_SERVLET);

        captchaCheckServlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + REGISTRATION_SERVLET);
    }

    @Test
    public void shouldRedirectOnLoginServletAndRecordPictureWithAllFieldsProperlyFilledWhenDoPostMethodWillBeCalled() throws IOException, ServletException, UserException {
        RegistrationForm registrationForm = new RegistrationForm();
        Part part = mock(Part.class);
        registrationForm.setAvatarPart(part);
        when(request.getParameter(JspConstants.USER_CAPTCHA_CODE)).thenReturn(VALID_CAPTCHA_CODE);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        when(session.getAttribute(SessionConstants.REGISTRATION_FORM)).thenReturn(registrationForm);
        when(captchaManager.getCaptcha(request, response)).thenReturn(CAPTCHA_PACK);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        when(avatarService.saveAvatar(part)).thenReturn(null);

        captchaCheckServlet.doPost(request, response);

        verify(avatarService).saveAvatar(part);
        verify(response).sendRedirect(request.getContextPath() + PRODUCT_SERVLET);
    }


}