package com.epam.preprod.pavlov.service.locale.impl;

import com.epam.preprod.pavlov.constant.CookieConstants;
import com.epam.preprod.pavlov.exception.ValidationException;
import com.epam.preprod.pavlov.service.locale.LocaleService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class CookieLocaleService implements LocaleService {
    private static final String INVALID_COOKIE_FORMAT = "Invalid cookie format";
    private static final Logger COOKIE_LOCALE_SERVICE_LOGGER = LoggerFactory.getLogger(CookieLocaleService.class);
    public static final int maxAge = 12000;
    public static final String COUNTRY_PARAMETER = "Country";
    public static final String LANGUAGE_PARAMETER = "Language";

    @Override
    public void saveLocale(Locale locale, HttpServletRequest request, HttpServletResponse response) {
        JSONObject localeJson = new JSONObject();
        localeJson.put(COUNTRY_PARAMETER, locale.getCountry());
        localeJson.put(LANGUAGE_PARAMETER, locale.getLanguage());
        System.out.println(localeJson);
        Cookie localeCookie = null;
        try {
            localeCookie = new Cookie(CookieConstants.LOCALE_COOKIE, URLEncoder.encode(localeJson.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            COOKIE_LOCALE_SERVICE_LOGGER.error("Invalid cookie format at the time of saving cookie attempt");
            throw new ValidationException(INVALID_COOKIE_FORMAT);
        }
        localeCookie.setMaxAge(maxAge);
        response.addCookie(localeCookie);
    }

    @Override
    public Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        Cookie searchedCookie = findParticularCookie(CookieConstants.LOCALE_COOKIE, request);
        if (Objects.isNull(searchedCookie)) {
            return null;
        }
        JSONObject jsonCookie = null;
        try {
            jsonCookie = new JSONObject(URLDecoder.decode(searchedCookie.getValue(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            COOKIE_LOCALE_SERVICE_LOGGER.error("Invalid cookie format at the time of reading cookie attempt");
            throw new ValidationException(INVALID_COOKIE_FORMAT);
        }
        return new Locale((String) jsonCookie.get(COUNTRY_PARAMETER), (String) jsonCookie.get(LANGUAGE_PARAMETER));
    }

    private Cookie findParticularCookie(String cookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            return null;
        }
        Cookie searchedCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                searchedCookie = cookie;
            }
        }
        return searchedCookie;
    }
}
