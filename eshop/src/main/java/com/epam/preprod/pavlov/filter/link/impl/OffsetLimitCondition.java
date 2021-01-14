package com.epam.preprod.pavlov.filter.link.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class OffsetLimitCondition extends MySQlSelectConditionTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffsetLimitCondition.class);
    public static final String OFFSET_INSTRUCTION_FORMAT = " LIMIT %s OFFSET %s ";
    private String productNumParameterName;
    private String pageNumParameterName;

    public OffsetLimitCondition(String productNumParameterName, String pageNumParameterName) {
        super(productNumParameterName, pageNumParameterName);
        this.pageNumParameterName = pageNumParameterName;
        this.productNumParameterName = productNumParameterName;
    }

    @Override
    protected String performConditionQuery(String currentResult, HttpServletRequest request) {
        String productNum = request.getParameter(productNumParameterName);
        String pageNum = request.getParameter(pageNumParameterName);
        if (!NumberUtils.isParsable(productNum) || !NumberUtils.isParsable(pageNum)) {
            LOGGER.error("Invalid offset value!");
            throw new RuntimeException("Invalid offset value!");
        }
        int prNum = NumberUtils.toInt(productNum);
        int pNum = NumberUtils.toInt(pageNum);
        return currentResult + String.format(OFFSET_INSTRUCTION_FORMAT, prNum, prNum * (pNum - 1));
    }
}
