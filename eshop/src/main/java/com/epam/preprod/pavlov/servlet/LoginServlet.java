package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.entity.LoginForm;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.exception.AuthorizationException;
import com.epam.preprod.pavlov.service.account.AccountService;
import com.epam.preprod.pavlov.service.additional.AvatarService;
import com.epam.preprod.pavlov.util.FormValidator;
import com.epam.preprod.pavlov.util.NotificationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

import static com.epam.preprod.pavlov.constant.NotificationContextConstants.LOGIN_ERROR_CONTAINER;
import static com.epam.preprod.pavlov.constant.SessionConstants.*;

@WebServlet("/login")
public class LoginServlet extends AbstractServlet {
    private AccountService accountService;
    private AvatarService avatarService;

    @Override
    public void init() {
        accountService = (AccountService) getServletContext().getAttribute(ContextConstants.ACCOUNT_SERVICE);
        avatarService = (AvatarService) getServletContext().getAttribute(ContextConstants.AVATAR_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NotificationUtil.cleanNotificationContext(req);
        forwardTo(req, resp, PathConstants.LOGIN_PAGE, PathConstants.ENTER_FRAME);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        LoginForm loginForm = new LoginForm();
        loginForm.parseRequest(req);
        FormValidator.validateLoginForm(loginForm, req, LOGIN_ERROR_CONTAINER);
        if (NotificationUtil.isNotificationContainerEmpty(LOGIN_ERROR_CONTAINER, req)) {
            executeLoginProcess(loginForm, req);
        }
        session.removeAttribute(CURRENT_PRODUCT_PAGE_RULES);
        if (NotificationUtil.isNotificationContainerEmpty(LOGIN_ERROR_CONTAINER, req)) {
            if (isThereUnfinishedOrder(req)) {
                session.removeAttribute(UNFINISHED_ORDERING);
                resp.sendRedirect(req.getContextPath() + PathConstants.MAKE_ORDER_SERVLET);
            } else {
                resp.sendRedirect(req.getContextPath() + PathConstants.PRODUCT_SERVLET);
            }
        } else {
            String caller = req.getParameter(RequestConstants.LOGIN_CALLER);
            switch (caller) {
                case RequestConstants.LOGIN_PAGE_CALLER:
                    resp.sendRedirect(req.getContextPath() + PathConstants.LOGIN_SERVLET);
                    break;
                case RequestConstants.LOGIN_FRAME_CALLER:
                    resp.sendRedirect(req.getContextPath() + PathConstants.PRODUCT_SERVLET);
                    break;
            }
        }
    }

    private void setCurrentSessionAvatarPath(User user, HttpSession session) {
        String pathToAvatar = avatarService.getPathToAvatar(user.getAvatar());
        session.setAttribute(CURRENT_AVATAR_PATH, pathToAvatar);
    }

    private void executeLoginProcess(LoginForm loginForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            User user = accountService.login(loginForm);
            if (!loginForm.getPassword().equals(user.getPassword())) {
                NotificationUtil.addNewMessage(LOGIN_ERROR_CONTAINER, "invalidLoginForm", "User by this credentials is not exist!", request);
            } else {
                session.setAttribute(CURRENT_USER, user);
                setCurrentSessionAvatarPath(user, session);
            }
        } catch (AuthorizationException e) {
            NotificationUtil.addNewMessage(LOGIN_ERROR_CONTAINER, "invalidLoginForm", e.getMessage(), request);
        }
    }

    private boolean isThereUnfinishedOrder(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object unfinishedOrderFlag = session.getAttribute(UNFINISHED_ORDERING);
        return Objects.nonNull(unfinishedOrderFlag);
    }
}
