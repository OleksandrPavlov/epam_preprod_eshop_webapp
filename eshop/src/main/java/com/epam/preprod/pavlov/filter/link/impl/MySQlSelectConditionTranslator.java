package com.epam.preprod.pavlov.filter.link.impl;

import com.epam.preprod.pavlov.filter.link.FilterLink;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public abstract class MySQlSelectConditionTranslator implements FilterLink<String, HttpServletRequest> {
    private static final String WHERE_PART = "WHERE ";
    private static final String AND_PART = "AND ";
    protected String[] parameterNames;
    protected MySQlSelectConditionTranslator next;

    public MySQlSelectConditionTranslator(String... parameterNames) {
        this.parameterNames = parameterNames;
    }

    protected boolean ableToServe(HttpServletRequest request) {
        for (String parameterName : parameterNames) {
            String parameterValue = request.getParameter(parameterName);
            if (Objects.isNull(parameterValue) || parameterValue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    protected boolean isNext() {
        return Objects.nonNull(next);
    }

    @Override
    public void setNext(FilterLink filterLink) {
        this.next = (MySQlSelectConditionTranslator) filterLink;
    }

    protected String addWhereOrAnd(String currentQuery) {
        String resultQuery;
        if (StringUtils.isBlank(currentQuery) || currentQuery.isEmpty()) {
            currentQuery = StringUtils.EMPTY;
            resultQuery = String.format("%s%s", currentQuery, WHERE_PART);
        } else {
            resultQuery = String.format("%s%s", currentQuery, AND_PART);
        }
        return resultQuery;
    }

    public String doFilter(HttpServletRequest request) {
        String result = StringUtils.EMPTY;
        if (isNext()) {
            result = next.doFilter(request);
        }
        if (ableToServe(request)) {
            result = performConditionQuery(result, request);
        }
        return result;
    }

    protected abstract String performConditionQuery(String currentResult, HttpServletRequest request);

}
