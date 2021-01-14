package com.epam.preprod.pavlov.constant;

public enum HttpCacheHeader {
    CACHE_CONTROL("Cache-Control"),
    EXPIRES("Expires"),
    PRAGMA("Pragma");
    private String name;

    HttpCacheHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
