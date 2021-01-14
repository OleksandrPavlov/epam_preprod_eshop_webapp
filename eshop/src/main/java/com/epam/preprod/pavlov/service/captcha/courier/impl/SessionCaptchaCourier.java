package com.epam.preprod.pavlov.service.captcha.courier.impl;

import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.service.captcha.courier.CaptchaCourier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionCaptchaCourier implements CaptchaCourier {
    @Override
    public void sendCaptcha(CaptchaPack pack, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        request.getSession().setAttribute(sessionId, pack);
    }

}
