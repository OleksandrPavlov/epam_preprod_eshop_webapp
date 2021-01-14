package com.epam.preprod.pavlov.wrapper;

import com.epam.preprod.pavlov.constant.RequestConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class CustomHttpRequestWrapper extends HttpServletRequestWrapper {
    private Locale currentLocale;
    private Vector<Locale> locales;

    public CustomHttpRequestWrapper(HttpServletRequest request, Locale locale) {
        super(request);
        locales = new Vector<>();
        locales.add(locale);
        this.currentLocale = locale;
    }

    @Override
    public Locale getLocale() {
        return currentLocale;
    }

    @Override
    public String getQueryString() {
        Map<String, String[]> map = this.getParameterMap();
        if (map.get(RequestConstants.LANG_PARAMETER) != null) {
            if (map.size() == 1) {
                return null;
            }
        }
        return super.getQueryString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomHttpRequestWrapper that = (CustomHttpRequestWrapper) o;
        return Objects.equals(currentLocale, that.currentLocale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentLocale);
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return locales.elements();
    }
}
