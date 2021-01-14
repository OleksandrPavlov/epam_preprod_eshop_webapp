package com.epam.preprod.pavlov.filter.http;

import com.epam.preprod.pavlov.constant.ApplicationInitConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.service.locale.LocaleService;
import com.epam.preprod.pavlov.service.locale.impl.CookieLocaleService;
import com.epam.preprod.pavlov.service.locale.impl.SessionLocaleService;
import com.epam.preprod.pavlov.util.ParserUtil;
import com.epam.preprod.pavlov.wrapper.CustomHttpRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class LocaleFilter extends AbstractFilter {
    private static final String RESOURCE_BUNDLE_NAME = "webApp";
    private Locale defaultLocale = new Locale("en", "EN");
    private Map<String, Locale> supportedLocales;
    private LocaleService localeService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        defineLocaleStoringStrategy(filterConfig);
        defineSupportedLocaleMap(filterConfig);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Locale locale = getLocale(request, response);
        request = new CustomHttpRequestWrapper(request, locale);
        localeService.saveLocale(locale, request, response);
        loadResourceBundle(locale, request);
        filterChain.doFilter(request, response);
    }

    public Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        Locale chosenLocale;
        chosenLocale = defineLocaleFromRequest(request);
        if (Objects.nonNull(chosenLocale)) {
            return chosenLocale;
        }
        chosenLocale = localeService.getLocale(request, response);
        if (Objects.nonNull(chosenLocale)) {
            return chosenLocale;
        }
        chosenLocale = getMostRelevantLocaleToBrowser(request.getLocales(), supportedLocales);
        return Objects.isNull(chosenLocale) ? defaultLocale : chosenLocale;
    }

    private Locale getMostRelevantLocaleToBrowser(Enumeration<Locale> browserLocales, Map<String, Locale> applicationLocales) {
        while (browserLocales.hasMoreElements()) {
            Locale currentBrowserLocale = browserLocales.nextElement();
            for (Map.Entry<String, Locale> entry : applicationLocales.entrySet()) {
                if (currentBrowserLocale.getLanguage().equals(entry.getValue().getLanguage()) &&
                        currentBrowserLocale.getCountry().equals(entry.getValue().getCountry())) {
                    return currentBrowserLocale;
                }
            }
        }
        return null;
    }

    private Locale defineLocaleFromRequest(HttpServletRequest request) {
        String lang = request.getParameter(RequestConstants.LANG_PARAMETER);
        return supportedLocales.get(lang);
    }

    private void defineSupportedLocaleMap(FilterConfig filterConfig) {
        supportedLocales = new HashMap<>();
        String stringLocaleRepresentation = filterConfig.getInitParameter(ApplicationInitConstants.ADJUSTED_LOCALES);
        List<Locale> locales = ParserUtil.parseLocales(stringLocaleRepresentation);
        locales.forEach((element) -> supportedLocales.put(element.getLanguage() + element.getCountry(), element));
    }

    private void defineLocaleStoringStrategy(FilterConfig filterConfig) {
        Map<String, LocaleService> storingStrategyMap = new HashMap<>();
        storingStrategyMap.put(ApplicationInitConstants.LOCALE_COOKIE_STORING_STRATEGY, new CookieLocaleService());
        storingStrategyMap.put(ApplicationInitConstants.LOCALE_SESSION_STORING_STRATEGY, new SessionLocaleService());
        String storingStrategy = filterConfig.getInitParameter(ApplicationInitConstants.LOCALE_STORING_STRATEGY);
        localeService = storingStrategyMap.get(storingStrategy);
    }

    private void loadResourceBundle(Locale locale, HttpServletRequest request) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, locale);
        request.setAttribute(RequestConstants.RESOURCE_BUNDLE, resourceBundle);
    }


}
