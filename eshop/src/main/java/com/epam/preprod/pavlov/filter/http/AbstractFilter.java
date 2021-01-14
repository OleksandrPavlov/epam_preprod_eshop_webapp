package com.epam.preprod.pavlov.filter.http;

import com.epam.preprod.pavlov.servlet.AbstractServlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException;

    protected void forwardTo(HttpServletRequest request, HttpServletResponse response, String innerPage, String outerPage) throws ServletException, IOException {
        request.setAttribute(AbstractServlet.INNER_PAGE_ATTRIBUTE_NAME, innerPage);
        request.getRequestDispatcher(outerPage).forward(request, response);
    }

    @Override
    public void destroy() {
    }
}
