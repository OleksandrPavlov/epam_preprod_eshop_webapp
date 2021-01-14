package com.epam.preprod.pavlov.filter.service;

import javax.servlet.http.HttpServletRequest;

public interface QueryGenerator {
    void init();

    String getProductSelectQueryByUrl(HttpServletRequest request);

    String getProductCountQueryByUrl(HttpServletRequest request);
}
