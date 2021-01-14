package com.epam.preprod.pavlov.servlet;


import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.entity.RegistrationForm;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.service.account.AccountService;
import com.epam.preprod.pavlov.util.FormValidator;
import com.epam.preprod.pavlov.util.NotificationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.epam.preprod.pavlov.constant.ErrorMessages.LOGIN_ALREADY_EXISTS;
import static com.epam.preprod.pavlov.constant.NotificationContextConstants.REGISTRATION_FORM_ERROR_CONTAINER;
import static com.epam.preprod.pavlov.constant.NotificationContextConstants.REGISTRATION_PROCESS_ERROR_CONTAINER;
import static com.epam.preprod.pavlov.constant.PathConstants.ENTER_FRAME;
import static com.epam.preprod.pavlov.constant.RequestConstants.*;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet("/registration")
public class RegistrationServlet extends AbstractServlet {
    private AccountService accountService;

    @Override
    public void init() throws ServletException {
        accountService = (AccountService) getServletContext().getAttribute(ContextConstants.ACCOUNT_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        RegistrationForm form = (RegistrationForm) session.getAttribute(SessionConstants.REGISTRATION_FORM);
        req.setAttribute(REGISTRATION_FORM, form);
        session.removeAttribute(SessionConstants.REGISTRATION_FORM);
        NotificationUtil.cleanNotificationContext(req);
        forwardTo(req, resp, PathConstants.REGISTRATION_PAGE, ENTER_FRAME);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        RegistrationForm form = new RegistrationForm();
        form.parseRequest(req, REGISTRATION_FORM_ERROR_CONTAINER);
        ResourceBundle currentResourceBundle = (ResourceBundle) req.getAttribute(RESOURCE_BUNDLE);
        FormValidator.validateRegistrationForm(currentResourceBundle, form, REGISTRATION_FORM_ERROR_CONTAINER, req);
        checkOnUnique(form, req);
        session.setAttribute(SessionConstants.REGISTRATION_FORM, form);
        if (NotificationUtil.isAllNotificationContainersEmpty(req)) {
            resp.sendRedirect(req.getContextPath() + PathConstants.CAPTCHA_SERVLET);
        } else {
            resp.sendRedirect(req.getContextPath() + PathConstants.REGISTRATION_SERVLET);
        }
    }

    private void checkOnUnique(RegistrationForm form, HttpServletRequest request) {
        if (NotificationUtil.isNotificationContainerEmpty(REGISTRATION_PROCESS_ERROR_CONTAINER, request)) {
            User userByLogin = accountService.getByLogin(form.getLogin());
            if (Objects.nonNull(userByLogin)) {
                NotificationUtil.addNewMessage(REGISTRATION_PROCESS_ERROR_CONTAINER, SAME_LOGIN, LOGIN_ALREADY_EXISTS, request);
            }
        }
    }
}
