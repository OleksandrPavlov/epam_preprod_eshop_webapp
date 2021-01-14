package com.epam.preprod.pavlov.service.captcha.courier.impl;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.service.captcha.courier.CaptchaCourier;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


public class ContextCaptchaCourier implements CaptchaCourier {
    private static String COOKIE_NAME = "captchaCookie";

    @Override
    public void sendCaptcha(CaptchaPack pack, HttpServletRequest request, HttpServletResponse response) {
        String sessionIdentifier = request.getSession().getId();
        ServletContext context = request.getSession().getServletContext();
        HashMap<String, CaptchaPack> captchaContainer = (HashMap<String, CaptchaPack>) context.getAttribute(ContextConstants.CAPTCHA_CONTAINER);
        captchaContainer.put(sessionIdentifier, pack);
        insertCookieToResponse(COOKIE_NAME, sessionIdentifier, response);
        addHiddenField(RequestConstants.CAPTCHA_ID_HIDDEN_FIELD, sessionIdentifier, request);
    }

    public void insertCookieToResponse(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(1000);
        response.addCookie(cookie);
    }

    public void addHiddenField(String name, String value, HttpServletRequest request) {
        request.setAttribute(name, value);
    }

}
