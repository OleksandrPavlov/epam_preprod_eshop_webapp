package com.epam.preprod.pavlov.filter.link.impl;

import javax.servlet.http.HttpServletRequest;

public class PhraseCondition extends MySQlSelectConditionTranslator {
    private static final String NAME_CONDITION_PART_FORMAT = "%s='%s' ";
    private String dbColumn;
    private String parameterName;


    public PhraseCondition(String dbColumn, String parameterName) {
        super(parameterName);
        this.dbColumn = dbColumn;
        this.parameterName = parameterName;
    }

    @Override
    protected String performConditionQuery(String currentResult, HttpServletRequest request) {
        String[] values = request.getParameterValues(parameterName);
        for (String val : values) {
            currentResult = addWhereOrAnd(currentResult);
            currentResult = currentResult + String.format(NAME_CONDITION_PART_FORMAT, dbColumn, val);
        }
        return currentResult;
    }

}
