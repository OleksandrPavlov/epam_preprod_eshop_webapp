package com.epam.preprod.pavlov.factory;

import com.epam.preprod.pavlov.service.captcha.courier.CaptchaCourier;
import com.epam.preprod.pavlov.service.captcha.destroyer.CaptchaDestroyer;
import com.epam.preprod.pavlov.service.captcha.supplier.CaptchaSupplier;

/**
 * @author Aleksandr Pavlov
 * @version 1.0
 * This interface needs to extracting of particular Captcha servants according to chosen strategy.
 */
public interface CaptchaServantFactory {
    /**
     * Obtaining particular CaptchaCourier
     *
     * @return particular Captcha Courier
     */
    CaptchaCourier getCaptchaCourier();

    /**
     * Obtaining particular CaptchaSupplier
     *
     * @return particular CaptchaSupplier
     */
    CaptchaSupplier getCaptureSupplier();

    /**
     * Obtaining particular CaptchaSupplier
     *
     * @return particular CaptchaDestroyer
     */
    CaptchaDestroyer getCaptureDestroyer();

}
