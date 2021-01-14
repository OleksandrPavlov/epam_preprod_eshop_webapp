package com.epam.preprod.pavlov.service.captcha.impl;

import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.factory.CaptchaServantFactory;
import com.epam.preprod.pavlov.service.captcha.CaptchaManager;
import com.epam.preprod.pavlov.service.captcha.CaptchaService;
import com.epam.preprod.pavlov.service.captcha.courier.CaptchaCourier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class CaptchaManagerImpl implements CaptchaManager {
    private CaptchaService captureService;
    private CaptchaServantFactory servantFactory;

    public CaptchaManagerImpl(CaptchaService captureService,
                              CaptchaServantFactory servantFactory) {
        this.captureService = captureService;
        this.servantFactory = servantFactory;

    }

    @Override
    public void recreateCaptcha(HttpServletRequest request, HttpServletResponse response, long liveTime) throws IOException {
        removeCaptcha(request,response);
        CaptchaPack pack = captureService.generateCapture();
        pack.setUsefulSecondInterval(liveTime);
        CaptchaCourier courier = servantFactory.getCaptchaCourier();
        courier.sendCaptcha(pack, request, response);
    }

    @Override
    public CaptchaPack removeCaptcha(HttpServletRequest request, HttpServletResponse response) {
        return servantFactory.getCaptureDestroyer().destroyCaptcha(request, response);
    }

    @Override
    public CaptchaPack getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        CaptchaPack pack = servantFactory.getCaptureSupplier().getCaptcha(request, response);
        if (isCaptchaAlive(pack)) {
            return pack;
        } else {
            return null;
        }
    }

    public static boolean isCaptchaAlive(CaptchaPack pack) {
        LocalDateTime captchaCreationTime = pack.getCreationTime();
        LocalDateTime captchaCreationTimePlusInterval = captchaCreationTime.plusSeconds(pack.getUsefulSecondInterval());
        LocalDateTime timeNow = LocalDateTime.now();
        return timeNow.compareTo(captchaCreationTimePlusInterval) <= 0;
    }

}
