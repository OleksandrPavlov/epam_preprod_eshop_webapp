package com.epam.preprod.pavlov.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ParserUtil {
    public static final String LOCALES_PATTERN = "((?<language>([a-zA-Z]{2}))_(?<country>([a-zA-Z]{2})))\\|?";
    public static final String LANGUAGE_GROUP = "language";
    public static final String COUNTRY_GROUP = "country";

    private ParserUtil() {
    }

    public static List<Locale> parseLocales(String stringLocales) {
        Pattern pattern = Pattern.compile(LOCALES_PATTERN);
        Matcher matcher = pattern.matcher(stringLocales);
        List<Locale> locales = new ArrayList<>();
        while (matcher.find()) {
            locales.add(new Locale(matcher.group(LANGUAGE_GROUP), matcher.group(COUNTRY_GROUP)));
        }
        return locales;
    }
}
