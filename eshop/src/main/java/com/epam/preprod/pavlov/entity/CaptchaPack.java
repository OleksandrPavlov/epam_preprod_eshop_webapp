package com.epam.preprod.pavlov.entity;

import java.time.LocalDateTime;

public class CaptchaPack {
    private String key;
    private String urlImage;
    private LocalDateTime creationTime;
    private long usefulSecondInterval;

    public CaptchaPack(String key, String urlImage) {
        this.key = key;
        this.urlImage = urlImage;
        creationTime = LocalDateTime.now();
    }

    public String getKey() {
        return key;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUsefulSecondInterval(long usefulSecondInterval) {
        this.usefulSecondInterval = usefulSecondInterval;
    }

    public long getUsefulSecondInterval() {
        return usefulSecondInterval;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}
