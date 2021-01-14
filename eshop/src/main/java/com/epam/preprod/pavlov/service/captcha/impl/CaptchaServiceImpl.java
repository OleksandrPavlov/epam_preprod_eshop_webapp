package com.epam.preprod.pavlov.service.captcha.impl;

import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.service.captcha.CaptchaService;
import com.github.cage.Cage;
import com.github.cage.GCage;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;


public class CaptchaServiceImpl implements CaptchaService {
    private static final String JSP_CAPTURE_PREFIX = "data:image/png;base64,";
    private static final int UPPER_BOUND = 9;
    private static final int LOWER_BOUND = 0;
    private int seed;
    private int keyLength;

    public CaptchaServiceImpl(int seed, int keyLength) {
        this.seed = seed;
        this.keyLength = keyLength;
    }

    @Override
    public CaptchaPack generateCapture() throws IOException {
        String key = getKey();
        Cage cage = new GCage();
        try (ByteArrayOutputStream arrayForStoringCaptchaByteStream = new ByteArrayOutputStream()) {
            cage.draw(key, arrayForStoringCaptchaByteStream);
            String captureUrl = JSP_CAPTURE_PREFIX + DatatypeConverter.printBase64Binary(arrayForStoringCaptchaByteStream.toByteArray());
            return new CaptchaPack(key, captureUrl);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getKey() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random(seed);
        for (int i = 0; i < keyLength; i++) {
            int randomNumber = random.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
            builder.append(randomNumber);
        }
        seed++;
        return builder.toString();
    }
}
