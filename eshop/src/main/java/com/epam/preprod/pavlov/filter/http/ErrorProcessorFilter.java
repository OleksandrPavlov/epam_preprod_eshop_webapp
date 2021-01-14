package com.epam.preprod.pavlov.filter.http;

import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.util.model.NotificationContainer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class ErrorProcessorFilter extends AbstractFilter {
    private static final Logger LOGGER = Logger.getLogger(ErrorProcessorFilter.class.getName());

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("ErrorProcessorFilter");
        HttpSession session = request.getSession();
        Object cleanErrorContainer = session.getAttribute(RequestConstants.CLEAN_ERROR_CONTEXT_FLAG);

        boolean cleanErrorContext;
        if (Objects.isNull(cleanErrorContainer)) {
            cleanErrorContext = false;
        } else {
            cleanErrorContext = (boolean) cleanErrorContainer;
        }
        if (cleanErrorContext) {
            Map<String, NotificationContainer> notificationContext = (Map<String, NotificationContainer>) request.getSession().getAttribute(SessionConstants.NOTIFICATION_CONTEXT);
            notificationContext.clear();
            session.setAttribute(RequestConstants.CLEAN_ERROR_CONTEXT_FLAG, false);
        }
        filterChain.doFilter(request, response);
    }
}
