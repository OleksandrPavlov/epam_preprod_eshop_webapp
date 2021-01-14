package com.epam.preprod.pavlov.util.model;

import java.util.HashMap;
import java.util.Map;

public class NotificationContainer {
    private Map<String,String> notificationContainer;
    private String name;

    public NotificationContainer(String name) {
        this.notificationContainer = new HashMap<>();
        this.name = name;
    }

    public Map<String, String> getNotificationContainer() {
        return notificationContainer;
    }

    public String getName() {
        return name;
    }
}
