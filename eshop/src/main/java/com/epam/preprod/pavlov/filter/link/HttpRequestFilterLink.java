package com.epam.preprod.pavlov.filter.link;

import javax.servlet.http.HttpServletRequest;

public interface HttpRequestFilterLink<T> {
    T doFilter(HttpServletRequest request);

    void setNext(HttpRequestFilterLink filterLink);
}
