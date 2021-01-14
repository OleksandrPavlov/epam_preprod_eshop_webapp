package com.epam.preprod.pavlov.service.locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Pavlov Oleksander
 * @version 1.0
 * This interface provides methods to storing locales in different kinds of scopes.
 */
public interface LocaleService {
    /**
     * This method saves passed locales to storage
     * @param locale will be saved
     * @param request object provides methods for access to different scopes
     * @param response object provides relations to different scopes related to response
     */
    void saveLocale(Locale locale, HttpServletRequest request, HttpServletResponse response);

    /**
     * This method take locale from particular storage(depends on implementation).
     * @param request  object provides methods for access to different scopes
     * @param response object provides relations to different scopes related to response
     * @return particular Locale object
     */
    Locale getLocale(HttpServletRequest request, HttpServletResponse response);
}
