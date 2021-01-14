package com.epam.preprod.pavlov.service.captcha;

import com.epam.preprod.pavlov.entity.CaptchaPack;

import java.io.IOException;

/**
 * @author Aleksandr Pavlov
 * @version 1.0
 * This interface provides method to generate of Captcha Pack.
 */
public interface CaptchaService {
    /**
     * Generates new CaptchaPack
     *
     * @return generated CaptchaPack
     * @throws IOException if some problems were occurred during captcha generating.
     */
    CaptchaPack generateCapture() throws IOException;
}
