package com.epam.preprod.pavlov.filter.link.impl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SortCondition extends MySQlSelectConditionTranslator {
    private static final String ORDER_FORMAT = "%sORDER BY %s ";
    private String sortParameter;
    private Map<String, String> parameterValueInColumn;

    public SortCondition(String sortParameter, Map<String, String> parameterValueInColumn) {
        super(sortParameter);
        this.parameterValueInColumn = parameterValueInColumn;
        this.sortParameter = sortParameter;
    }

    @Override
    protected String performConditionQuery(String currentResult, HttpServletRequest request) {
        String value = parameterValueInColumn.get(request.getParameter(sortParameter));
        return String.format(ORDER_FORMAT, currentResult, value);
    }
}
