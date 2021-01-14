package com.epam.preprod.pavlov.filter.link.impl;


import com.epam.preprod.pavlov.exception.ApplicationException;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class PriceCondition extends MySQlSelectConditionTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceCondition.class);

    private static final String PRICE_CONDITION_FORMAT = "%s BETWEEN %s AND %s ";
    private String maxPriceParameterName;
    private String minPriceParameterName;
    private String dbColumn;

    public PriceCondition(String maxPriceParameterName, String minPriceParameterName, String dbColumn) {
        super(maxPriceParameterName, minPriceParameterName);
        this.dbColumn = dbColumn;
        this.maxPriceParameterName = maxPriceParameterName;
        this.minPriceParameterName = minPriceParameterName;
    }

    @Override
    protected String performConditionQuery(String currentResult, HttpServletRequest request) {
        currentResult = addWhereOrAnd(currentResult);
        String maxPriceStringValue = request.getParameter(maxPriceParameterName);
        String minPriceStringValue = request.getParameter(minPriceParameterName);
        if (!NumberUtils.isParsable(maxPriceStringValue) || !NumberUtils.isParsable(minPriceStringValue)) {
            LOGGER.error("Invalid price value!");
            throw new ApplicationException("Invalid price value!");
        }
        double max = NumberUtils.toDouble(maxPriceStringValue);
        double min = NumberUtils.toDouble(minPriceStringValue);
        return currentResult + String.format(PRICE_CONDITION_FORMAT, dbColumn, min, max);
    }
}
