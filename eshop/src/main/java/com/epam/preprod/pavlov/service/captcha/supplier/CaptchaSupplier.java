package com.epam.preprod.pavlov.service.captcha.supplier;

import com.epam.preprod.pavlov.entity.CaptchaPack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aleksandr Pavlov
 * @version 1.0
 * Objects implementing this interface, able to send captchaPack to different destinations in different ways.
 */
public interface CaptchaSupplier {
    /**
     * Sends CaptchaPack.
     *
     * @param request  provides some different scopes of storing.
     * @param response provides opportunity of writing some stuff in response.
     * @return CaptchaPack that was found and Null in another case.
     */
    CaptchaPack getCaptcha(HttpServletRequest request, HttpServletResponse response);

}
