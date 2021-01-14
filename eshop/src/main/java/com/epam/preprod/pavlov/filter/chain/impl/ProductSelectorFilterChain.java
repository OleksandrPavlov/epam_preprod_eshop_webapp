package com.epam.preprod.pavlov.filter.chain.impl;

import com.epam.preprod.pavlov.filter.chain.FilterChain;
import com.epam.preprod.pavlov.filter.link.impl.MySQlSelectConditionTranslator;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class ProductSelectorFilterChain implements FilterChain<MySQlSelectConditionTranslator, String> {
    private MySQlSelectConditionTranslator firstLink;
    private MySQlSelectConditionTranslator lastLink;

    @Override
    public void addLink(MySQlSelectConditionTranslator handler) {
        if (Objects.isNull(firstLink)) {
            firstLink = handler;
        } else {
            lastLink.setNext(handler);
        }
        lastLink = handler;
    }

    @Override
    public String doChain(HttpServletRequest request) {
        return firstLink.doFilter(request);
    }
}
