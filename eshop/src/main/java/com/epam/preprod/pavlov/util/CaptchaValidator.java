package com.epam.preprod.pavlov.util;

import com.epam.preprod.pavlov.constant.ErrorMessages;
import com.epam.preprod.pavlov.entity.CaptchaPack;

import javax.servlet.http.HttpServletRequest;

import static com.epam.preprod.pavlov.constant.RequestConstants.CAPTCHA_EXPIRED;
import static com.epam.preprod.pavlov.constant.RequestConstants.CAPTCHA_NOT_VALID;

public class CaptchaValidator {
    public static void validCaptcha(CaptchaPack pack, String userInput, String noteContainerName, HttpServletRequest request) {
        if (pack == null) {
            NotificationUtil.addNewMessage(noteContainerName, CAPTCHA_EXPIRED, ErrorMessages.CAPTCHA_EXPIRED, request);
            return;
        }
        if (!pack.getKey().equals(userInput)) {
            NotificationUtil.addNewMessage(noteContainerName, CAPTCHA_NOT_VALID,  ErrorMessages.CAPTCHA_NOT_VALID, request);
        }
    }
}
