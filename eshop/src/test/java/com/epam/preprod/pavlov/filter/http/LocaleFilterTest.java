package com.epam.preprod.pavlov.filter.http;

import com.epam.preprod.pavlov.constant.ApplicationInitConstants;
import com.epam.preprod.pavlov.constant.CookieConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.wrapper.CustomHttpRequestWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocaleFilterTest {
    private static final String SUPPORTED_LOCALE_INIT = "en_EN|ru_RU";
    private static final String INCORRECT_LOCALS = "en_MT";
    private static final Cookie RUSSIAN_LOCALE_COOKIE = new Cookie(CookieConstants.LOCALE_COOKIE, "{\"Language\":\"ru\",\"Country\":\"RU\"}");
    private static final Locale RUSSIAN_LOCALE = new Locale("ru", "RU");
    private static final Locale ENGLISH_LOCALE = new Locale("en", "EN");


    @Mock
    private FilterConfig filterConfig;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;


    @Test
    public void shouldChooseLocaleFromSessionWhenSessionStoringModeOn() throws IOException, ServletException {
        filterConfiguring(ApplicationInitConstants.LOCALE_SESSION_STORING_STRATEGY,SUPPORTED_LOCALE_INIT);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.CURRENT_LOCALE)).thenReturn(RUSSIAN_LOCALE);

        CustomHttpRequestWrapper expectedRequestWrapper = new CustomHttpRequestWrapper(request, RUSSIAN_LOCALE);
        filterInitializationAndExecuting();

        verify(session).getAttribute(SessionConstants.CURRENT_LOCALE);
        verify(filterChain).doFilter(expectedRequestWrapper, response);
    }

    @Test
    public void shouldChooseLocaleFromCookieWhenCookieStoringModeOn() throws ServletException, IOException {
        filterConfiguring(ApplicationInitConstants.LOCALE_COOKIE_STORING_STRATEGY,SUPPORTED_LOCALE_INIT);
        when(request.getCookies()).thenReturn(new Cookie[]{RUSSIAN_LOCALE_COOKIE});

        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(request, RUSSIAN_LOCALE);
        filterInitializationAndExecuting();

        verify(filterChain).doFilter(requestWrapper, response);
        verify(request).getCookies();

    }

    @Test
    public void shouldChooseLocaleBasedOnRequest() throws ServletException, IOException {
        filterConfiguring(ApplicationInitConstants.LOCALE_COOKIE_STORING_STRATEGY,SUPPORTED_LOCALE_INIT);
        when(request.getParameter(RequestConstants.LANG_PARAMETER)).thenReturn("ruRU");

        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(request, RUSSIAN_LOCALE);
        filterInitializationAndExecuting();

        verify(filterChain).doFilter(requestWrapper, response);
        verify(request).getParameter(RequestConstants.LANG_PARAMETER);

    }

    @Test
    public void shouldChooseLocaleMostRelevantToBrowser() throws ServletException, IOException {
        filterConfiguring(ApplicationInitConstants.LOCALE_COOKIE_STORING_STRATEGY,SUPPORTED_LOCALE_INIT);
        when(request.getLocales()).thenReturn(prepareLocales());

        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(request, RUSSIAN_LOCALE);
        filterInitializationAndExecuting();

        verify(filterChain).doFilter(requestWrapper, response);
    }

    @Test
    public void shouldChooseDefaultLocaleWhenNoOneRelevantToBrowser() throws ServletException, IOException {
        filterConfiguring(ApplicationInitConstants.LOCALE_COOKIE_STORING_STRATEGY,INCORRECT_LOCALS);
        when(request.getLocales()).thenReturn(prepareLocales());

        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(request, ENGLISH_LOCALE);
        filterInitializationAndExecuting();

        verify(filterChain).doFilter(requestWrapper, response);
    }

    @Test
    public void shouldChooseLocaleWhenIncorrectLocaleCameWithRequest() throws IOException, ServletException {
        filterConfiguring(ApplicationInitConstants.LOCALE_SESSION_STORING_STRATEGY,SUPPORTED_LOCALE_INIT);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(RequestConstants.LANG_PARAMETER)).thenReturn("rlRL");
        when(request.getLocales()).thenReturn(prepareLocales());

        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(request, RUSSIAN_LOCALE);
        filterInitializationAndExecuting();

        verify(filterChain).doFilter(requestWrapper, response);
    }

    private Enumeration<Locale> prepareLocales() {
        Vector<Locale> locales = new Vector<>();
        locales.add(new Locale("ar", "QA"));
        locales.add(new Locale("fi", "FI"));
        locales.add(new Locale("ru", "RU"));
        locales.add(new Locale("en", "EN"));
        return locales.elements();
    }

    private void filterConfiguring(String localStoringStrategy, String adjustedLocales) {
        when(filterConfig.getInitParameter(ApplicationInitConstants.LOCALE_STORING_STRATEGY))
                .thenReturn(localStoringStrategy);
        when(filterConfig.getInitParameter(ApplicationInitConstants.ADJUSTED_LOCALES))
                .thenReturn(adjustedLocales);
    }
    private void filterInitializationAndExecuting() throws ServletException, IOException {
        LocaleFilter localeFilter = new LocaleFilter();
        localeFilter.init(filterConfig);
        localeFilter.doFilter(request, response, filterChain);
    }

}