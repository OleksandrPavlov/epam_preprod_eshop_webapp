package com.epam.preprod.pavlov.service.captcha.supplier.impl;

import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.service.captcha.supplier.CaptchaSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionCaptchaSupplier implements CaptchaSupplier {

    @Override
    public CaptchaPack getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        return (CaptchaPack) session.getAttribute(sessionId);
    }


}
