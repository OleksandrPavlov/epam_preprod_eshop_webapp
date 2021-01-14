package com.epam.preprod.pavlov.constant;

import java.io.File;

public class ApplicationInitConstants {
    private ApplicationInitConstants() {
    }

    public static final String APPLICATION_PROPERTIES_FILE = "application.properties";
    public static final String DB_DRIVER = "db.driver";
    public static final String DB_URL = "db.url";
    public static final String DB_USERNAME = "db.username";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_INIT_POOL_SIZE = "db.poolSize";
    public static final String DB_MAX_POOL_SIZE = "db.poolSize";
    public static final String AVATARS_FOLDER = "path.avatar";

    public static final String LOCALE_STORING_STRATEGY = "storingStrategy";
    public static final String LOCALE_COOKIE_STORING_STRATEGY = "cookie";
    public static final String LOCALE_SESSION_STORING_STRATEGY = "session";
    public static final String ADJUSTED_LOCALES = "adjustedLocales";
}
