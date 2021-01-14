package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.constant.NotificationContextConstants;
import com.epam.preprod.pavlov.constant.ErrorMessages;
import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.entity.RegistrationForm;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.exception.UserException;
import com.epam.preprod.pavlov.factory.UserFactory;
import com.epam.preprod.pavlov.service.account.AccountService;
import com.epam.preprod.pavlov.service.additional.AvatarService;
import com.epam.preprod.pavlov.service.captcha.CaptchaManager;
import com.epam.preprod.pavlov.util.CaptchaValidator;
import com.epam.preprod.pavlov.util.NotificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Objects;

import static com.epam.preprod.pavlov.constant.CaptchaConfigConstants.CAPTCHA_ALIVE_INTERVAL;
import static com.epam.preprod.pavlov.constant.ContextConstants.ACCOUNT_SERVICE;
import static com.epam.preprod.pavlov.constant.ContextConstants.CAPTCHA_MANAGER;
import static com.epam.preprod.pavlov.constant.ErrorMessages.USER_ALREADY_EXISTS;
import static com.epam.preprod.pavlov.constant.JspConstants.USER_CAPTCHA_CODE;
import static com.epam.preprod.pavlov.constant.PathConstants.*;
import static com.epam.preprod.pavlov.constant.RequestConstants.ALREADY_EXIST_ERROR_TITLE;
import static com.epam.preprod.pavlov.constant.RequestConstants.SAME_LOGIN;
import static com.epam.preprod.pavlov.constant.SessionConstants.REGISTRATION_FORM;

@WebServlet("/captcha")
public class CaptchaCheckServlet extends AbstractServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaCheckServlet.class);
    private AccountService accountService;
    private AvatarService avatarService;
    private CaptchaManager captchaManager;

    @Override
    public void init() {
        LOGGER.info("Initializing CaptchaCheckServlet...");
        captchaManager = (CaptchaManager) getServletContext().getAttribute(CAPTCHA_MANAGER);
        accountService = (AccountService) getServletContext().getAttribute(ACCOUNT_SERVICE);
        avatarService = (AvatarService) getServletContext().getAttribute(ContextConstants.AVATAR_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        captchaManager.recreateCaptcha(req, resp, CAPTCHA_ALIVE_INTERVAL);
        forwardTo(req, resp, CAPTCHA_PAGE_PATH, ENTER_FRAME);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userInput = req.getParameter(USER_CAPTCHA_CODE);
        HttpSession session = req.getSession();
        RegistrationForm form = (RegistrationForm) session.getAttribute(REGISTRATION_FORM);
        CaptchaPack pack = captchaManager.getCaptcha(req, resp);
        CaptchaValidator.validCaptcha(pack, userInput, NotificationContextConstants.CAPTCHA_CHECK_NOTE, req);
        if (NotificationUtil.isAllNotificationContainersEmpty(req)) {
            try {
                User userFromDataBase = accountService.getByLogin(form.getLogin());
                if (Objects.nonNull(userFromDataBase)) {
                    NotificationUtil.addNewMessage( NotificationContextConstants.CAPTCHA_CHECK_NOTE,SAME_LOGIN,ErrorMessages.LOGIN_ALREADY_EXISTS,req);
                } else {
                    saveAvatarAndWriteNameToForm(form);
                    accountService.create(UserFactory.createUser(form));
                    session.removeAttribute(REGISTRATION_FORM);
                    resp.sendRedirect(req.getContextPath() + PRODUCT_SERVLET);
                    return;
                }
            } catch (UserException e) {
                NotificationUtil.addNewMessage( NotificationContextConstants.CAPTCHA_CHECK_NOTE,ALREADY_EXIST_ERROR_TITLE,USER_ALREADY_EXISTS,req);
                deleteAvatar(form);
            }
        }
        resp.sendRedirect(req.getContextPath() + REGISTRATION_SERVLET);
    }

    private void saveAvatarAndWriteNameToForm(RegistrationForm form) {
        Part part = form.getAvatarPart();
        form.setAvatarName(avatarService.saveAvatar(part));
    }

    private void deleteAvatar(RegistrationForm form ) {
        String pictureName = form.getAvatarName();
        avatarService.deleteAvatar(pictureName);
    }

}
