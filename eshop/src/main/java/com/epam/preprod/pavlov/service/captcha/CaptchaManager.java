package com.epam.preprod.pavlov.service.captcha;

import com.epam.preprod.pavlov.entity.CaptchaPack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * THis interface provides methods to manage by life cycle of Captcha Pack and storing it in any destinations.
 *
 * @author Aleksandr Pavlov
 * @version 1.0
 */
public interface CaptchaManager {
    /**
     * This method creates new Captcha with specified live time and removes old Captcha in the same way.
     *
     * @param request    provides some different scopes of storing.
     * @param response   provides opportunity of writing some stuff in response.
     * @param actualTime live time of captcha after which Captcha will represented as stitched.
     * @throws IOException if some internal IOException was occurred.
     */
    void recreateCaptcha(HttpServletRequest request, HttpServletResponse response, long actualTime) throws IOException;

    /**
     * Removes existing captcha.
     *
     * @param request  provides some different scopes of storing.
     * @param response provides opportunity of writing some stuff in response.
     * @return removed captcha.
     */
    CaptchaPack removeCaptcha(HttpServletRequest request, HttpServletResponse response);

    /**
     * Obtaining of existing captcha or Null if is no there.
     *
     * @param request  provides some different scopes of storing.
     * @param response provides opportunity of writing some stuff in response.
     * @return CaptchaPack or Null.
     */
    CaptchaPack getCaptcha(HttpServletRequest request, HttpServletResponse response);

}
