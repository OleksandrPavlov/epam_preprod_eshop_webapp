package com.epam.preprod.pavlov.factory.impl;

import com.epam.preprod.pavlov.factory.CaptchaServantFactory;
import com.epam.preprod.pavlov.service.captcha.courier.CaptchaCourier;
import com.epam.preprod.pavlov.service.captcha.destroyer.CaptchaDestroyer;
import com.epam.preprod.pavlov.service.captcha.supplier.CaptchaSupplier;

import java.util.Map;

public class CaptchaServantFactoryImpl implements CaptchaServantFactory {
    private Map<String, CaptchaCourier> courierMap;
    private Map<String, CaptchaSupplier> supplierMap;
    private Map<String, CaptchaDestroyer> destroyerMap;
    private String preferredStoringStrategy;

    public CaptchaServantFactoryImpl(Map<String, CaptchaCourier> courierHashMap,
                                     Map<String, CaptchaSupplier> supplierMap,
                                     Map<String, CaptchaDestroyer> destroyerMap,
                                     String preferredStoringStrategy) {
        this.courierMap = courierHashMap;
        this.supplierMap = supplierMap;
        this.destroyerMap = destroyerMap;
        this.preferredStoringStrategy = preferredStoringStrategy;
    }

    @Override
    public CaptchaCourier getCaptchaCourier() {
        return courierMap.get(preferredStoringStrategy);
    }

    @Override
    public CaptchaSupplier getCaptureSupplier() {
        return supplierMap.get(preferredStoringStrategy);
    }

    @Override
    public CaptchaDestroyer getCaptureDestroyer() {
        return destroyerMap.get(preferredStoringStrategy);
    }

}
