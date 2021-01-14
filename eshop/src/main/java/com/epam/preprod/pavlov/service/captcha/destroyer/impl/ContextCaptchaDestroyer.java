package com.epam.preprod.pavlov.service.captcha.destroyer.impl;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.service.captcha.destroyer.CaptchaDestroyer;
import com.epam.preprod.pavlov.service.captcha.impl.CaptchaManagerImpl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ContextCaptchaDestroyer implements CaptchaDestroyer {
    public static final Logger logger = Logger.getLogger(ContextCaptchaDestroyer.class.getName());

    @Override
    public CaptchaPack destroyCaptcha(HttpServletRequest request, HttpServletResponse response) {
        ServletContext context = request.getServletContext();
        String sessionId = request.getSession().getId();
        HashMap<String, CaptchaPack> container = (HashMap<String, CaptchaPack>) context.getAttribute(ContextConstants.CAPTCHA_CONTAINER);
        CaptchaPack removedPack = container.remove(sessionId);
        removeOldest(container);
        logger.info("Removing captcha from Servlet Context...");
        return removedPack;
    }

    private void removeOldest(HashMap<String, CaptchaPack> captchaContainer) {
        Map<String, CaptchaPack> filteredCaptchaContainer = captchaContainer.entrySet().stream().filter(captcha -> CaptchaManagerImpl.isCaptchaAlive(captcha.getValue())).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }


}
