package com.epam.preprod.pavlov.filter.http;

import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.entity.secutity.Security;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class SecurityFilter extends AbstractFilter {
    private static final Logger LOGGER = Logger.getLogger(SecurityFilter.class.getName());
    public static final String INAPPROPRIATE_ROLE_MESSAGE = "Access denied! Not appropriate role";
    private static final String STATIC_URL_PREFIX = "/static";
    private String pathToSecurityXML;
    private Security security;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        pathToSecurityXML = filterConfig.getServletContext().getRealPath("/") + "/WEB-INF/security.xml";
        LOGGER.info("Distributed access configured in the file: " + pathToSecurityXML);
        security = Security.forFile(new File(pathToSecurityXML));
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("Going trough SecurityFilter...");
        LOGGER.info("By full request path: " + request.getRequestURL() + "?" + request.getQueryString());

        String requestUri = request.getRequestURI();
        HttpSession session = request.getSession();
        if (!requestUri.contains(STATIC_URL_PREFIX)) {
            LOGGER.info("SecurityFilter# Current url contain /static component!");
            if (!isRestrictedUrl(requestUri)) {
                filterChain.doFilter(request, response);
                return;
            }
            User currentUser = (User) request.getSession().getAttribute(SessionConstants.CURRENT_USER);
            if (Objects.isNull(currentUser)) {
                LOGGER.info("SecurityFilter# User is not defined!");
                session.setAttribute(SessionConstants.DANGLING_PAGE_REQUEST, request.getRequestURL() + "?" + request.getQueryString());
                response.sendRedirect(request.getContextPath() + PathConstants.ENTER_SERVLET);
                return;
            }
            if (!haveAppropriateRole(currentUser, requestUri)) {
                LOGGER.info("SecurityFilter# Attempt to get unavailable resources!");
                session.setAttribute(SessionConstants.ERROR_MESSAGE, INAPPROPRIATE_ROLE_MESSAGE);
                response.sendRedirect(request.getContextPath() + PathConstants.ERROR_SERVLET);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isRestrictedUrl(String url) {
        return security.getConstraints().stream().anyMatch(element -> url.matches(element.getUrlPattern()));
    }

    private boolean haveAppropriateRole(User user, final String url) {
        return security.getConstraints()
                .stream()
                .filter(element -> url.matches(element.getUrlPattern()))
                .anyMatch(element -> element.getRoles()
                        .stream()
                        .anyMatch(role -> user.getRoles().contains(role)));
    }
}
