package com.epam.preprod.pavlov.filter.http;


import com.epam.preprod.pavlov.constant.HttpCacheHeader;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OffCacheFilter extends AbstractFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        response.setHeader(HttpCacheHeader.CACHE_CONTROL.getName(), String.format("%s,%s,%s", "no-cache", "no-store", "must-revalidate"));
        response.setHeader(HttpCacheHeader.PRAGMA.getName(), "no-cache");
        response.setDateHeader(HttpCacheHeader.EXPIRES.getName(), 0L);
        filterChain.doFilter(request, response);
    }
}
