package com.epam.preprod.pavlov.service.locale.impl;

import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.service.locale.LocaleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class SessionLocaleService implements LocaleService {

    @Override
    public void saveLocale(Locale locale, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.CURRENT_LOCALE, locale);
    }

    @Override
    public Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return (Locale) session.getAttribute(SessionConstants.CURRENT_LOCALE);
    }
  
}
