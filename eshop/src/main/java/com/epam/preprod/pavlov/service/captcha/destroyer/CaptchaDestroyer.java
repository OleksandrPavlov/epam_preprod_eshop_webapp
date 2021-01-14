package com.epam.preprod.pavlov.service.captcha.destroyer;

import com.epam.preprod.pavlov.entity.CaptchaPack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aleksandr Pavlov
 * @version 1.0
 * Objects implementing this interface, able to destroy captchaPack from different
 * destinations in different ways(depends of implementation).
 */
public interface CaptchaDestroyer {
    /**
     * Destroys CaptchaPack.
     *
     * @param request provides some different scopes of storing.
     * @param response used for placing some configuration setups.
     * @return Captcha Pack that was removed and Null in another case(remove operation was fail).
     */
    CaptchaPack destroyCaptcha(HttpServletRequest request, HttpServletResponse response);
}
