package com.epam.preprod.pavlov.filter.container;

import javax.servlet.http.HttpServletRequest;

public interface FilterChain<H, R> {
    void addLink(H handler);

    R doChain(HttpServletRequest request);
}
