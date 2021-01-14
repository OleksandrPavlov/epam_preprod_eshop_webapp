package com.epam.preprod.pavlov.filter.link.impl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class OrderByCondition extends MySQlSelectConditionTranslator {
    public static final String ORDER_BY = " ORDER BY ";
    private Map<String, String> urlAttributeCompliance;

    public OrderByCondition(Map<String, String> urlAttributeCompliance, String... parameterNames) {
        super(parameterNames);
        this.urlAttributeCompliance = urlAttributeCompliance;
    }

    @Override
    protected String performConditionQuery(String currentResult, HttpServletRequest request) {
        String orderByColumn = request.getParameter(parameterNames[0]);
        return currentResult + ORDER_BY + urlAttributeCompliance.get(orderByColumn);
    }
}
