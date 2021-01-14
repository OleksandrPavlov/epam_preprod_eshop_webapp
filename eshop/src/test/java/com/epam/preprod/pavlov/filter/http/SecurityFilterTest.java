package com.epam.preprod.pavlov.filter.http;


import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.entity.secutity.Constraint;
import com.epam.preprod.pavlov.entity.secutity.Security;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SecurityFilterTest {
    private static final String STATIC_CONTENT_REQUEST_URI = "/static/something";
    private static final String NOT_UNDER_SECURITY_URI = "/not/secure";
    private static final String UNDER_ADMIN_ROLE_URI = "/admin/secure";
    private static final String UNDER_ADMIN_ROLE_URL = "http://localehost:8080/admin/secure";
    private static final String UNDER_USER_ROLE_URL = "/user/secure";
    private static final String STRING_QUERY = "some=some";
    private static final String CONTEXT_PATH = "context/";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Security security;
    @Mock
    private HttpSession session;
    @Mock
    private FilterConfig filterConfig;
    @Mock
    private ServletContext context;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    private SecurityFilter securityFilter;
    private User admin;
    private User user;
    private List<Constraint> constraints;

    @Before
    public void init() {
        admin = new User();
        admin.setRoles(Stream.of("user", "admin").collect(Collectors.toList()));

        user = new User();
        user.setRoles(Stream.of("user").collect(Collectors.toList()));

        constraints = new ArrayList<>();
        Constraint constraintA = new Constraint();
        constraintA.setRoles(Stream.of("admin", "user").collect(Collectors.toList()));
        constraintA.setUrlPattern("/user/*.*");

        Constraint constraintB = new Constraint();
        constraintB.setRoles(Stream.of("admin").collect(Collectors.toList()));
        constraintB.setUrlPattern("/admin/*.*");

        constraints.add(constraintA);
        constraints.add(constraintB);
    }

    @Test
    public void shouldSkipOnWhenStaticContentRequested() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn(STATIC_CONTENT_REQUEST_URI);
        securityFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void shouldSkipOnIfRequestURLIsNotInSecureList() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn(NOT_UNDER_SECURITY_URI);
        when(security.getConstraints()).thenReturn(constraints);
        securityFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void shouldCheckUserAuthorizationIfUrlBeingUnderSecurity() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn(UNDER_ADMIN_ROLE_URI);
        when(security.getConstraints()).thenReturn(constraints);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.CURRENT_USER)).thenReturn(null);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        securityFilter.doFilter(request, response, filterChain);
        verify(session).getAttribute(SessionConstants.CURRENT_USER);
    }

    @Test
    public void shouldRedirectOnEnterServletWhenUserNotAuthorized() throws IOException, ServletException {
        shouldCheckUserAuthorizationIfUrlBeingUnderSecurity();
        verify(response).sendRedirect(CONTEXT_PATH+PathConstants.ENTER_SERVLET);
    }

    @Test
    public void shouldCheckIfUserHaveAppropriateRoleWhenUserAuthorized() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn(UNDER_ADMIN_ROLE_URI);
        when(security.getConstraints()).thenReturn(constraints);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.CURRENT_USER)).thenReturn(admin);
        securityFilter.doFilter(request, response, filterChain);
        verify(security, times(2)).getConstraints();
    }

    @Test
    public void shouldRedirectOnErrorPageWhenUserHaveNotAppropriateRole() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn(UNDER_ADMIN_ROLE_URI);
        when(security.getConstraints()).thenReturn(constraints);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.CURRENT_USER)).thenReturn(user);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        securityFilter.doFilter(request, response, filterChain);
        verify(security, times(2)).getConstraints();
        verify(session).setAttribute(SessionConstants.ERROR_MESSAGE, SecurityFilter.INAPPROPRIATE_ROLE_MESSAGE);
        verify(response).sendRedirect(CONTEXT_PATH + PathConstants.ERROR_SERVLET);
    }

    @Test
    public void shouldDoChainIfUserHaveAppropriateRole() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn(UNDER_USER_ROLE_URL);
        when(security.getConstraints()).thenReturn(constraints);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.CURRENT_USER)).thenReturn(user);
        securityFilter.doFilter(request, response, filterChain);
        verify(security, times(2)).getConstraints();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void shouldSaveDanglingRequestURLToSessionWhenUserIsNotAuthorized() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn(UNDER_ADMIN_ROLE_URI);
        when(security.getConstraints()).thenReturn(constraints);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.CURRENT_USER)).thenReturn(null);
        StringBuffer stringBuffer = new StringBuffer(UNDER_ADMIN_ROLE_URL);
        when(request.getRequestURL()).thenReturn(stringBuffer);
        when(request.getQueryString()).thenReturn(STRING_QUERY);

        securityFilter.doFilter(request, response, filterChain);
        verify(session).setAttribute(SessionConstants.DANGLING_PAGE_REQUEST, UNDER_ADMIN_ROLE_URL + "?" + STRING_QUERY);
    }
}