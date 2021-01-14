package com.epam.preprod.pavlov.service.captcha.courier;

import com.epam.preprod.pavlov.entity.CaptchaPack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aleksandr Pavlov
 * @version 1.0
 * Objects implementing this interface, able to send captchaPack to different destinations in different ways.
 */
public interface CaptchaCourier {
    /**
     * Sends CaptchaPack.
     *
     * @param pack     CaptchaPack that will be sent.
     * @param request  provides some different scopes of storing.
     * @param response provides opportunity of writing some stuff in response.
     */
    void sendCaptcha(CaptchaPack pack, HttpServletRequest request, HttpServletResponse response);

}
