package com.epam.preprod.pavlov.service.captcha.supplier.impl;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.service.captcha.supplier.CaptchaSupplier;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ContextCaptchaSupplier implements CaptchaSupplier {
    @Override
    public CaptchaPack getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        ServletContext context = request.getServletContext();
        HashMap<String, CaptchaPack> captchaPackHashMap = (HashMap<String, CaptchaPack>) context.getAttribute(ContextConstants.CAPTCHA_CONTAINER);
        CaptchaPack pack = captchaPackHashMap.get(sessionId);
        return pack;
    }
}
