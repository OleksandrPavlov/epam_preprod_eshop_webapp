package com.epam.preprod.pavlov.service.captcha.destroyer.impl;

import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.service.captcha.destroyer.CaptchaDestroyer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SessionCaptchaDestroyer implements CaptchaDestroyer {
    @Override
    public CaptchaPack destroyCaptcha(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        CaptchaPack pack = (CaptchaPack) session.getAttribute(sessionId);
        session.removeAttribute(sessionId);
        return pack;
    }

}
