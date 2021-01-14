package com.epam.preprod.pavlov.filter.link.impl;

import com.epam.preprod.pavlov.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriceConditionTest {
    public static final String MAX_PRICE_PARAMETER_NAME = "maxPrice";
    public static final String MIN_PRICE_PARAMETER_NAME = "minPrice";
    public static final String MIN_PRICE_PARAMETER_VALUE = "16.0";
    public static final String MAX_PRICE_PARAMETER_VALUE = "23.0";
    public static final String NOT_PARSABLE_PRICE_PARAMETER_VALUE = "76s";
    public static final String COLUMN_NAME = "column";

    @Mock
    private HttpServletRequest request;
    private PriceCondition filter;

    @Before
    public void init() {
        filter = new PriceCondition(MAX_PRICE_PARAMETER_NAME, MIN_PRICE_PARAMETER_NAME, COLUMN_NAME);
    }

    @Test
    public void shouldCreateCorrectQueryWhenMethodCalledWithParsableParameters() {
        when(request.getParameter(MAX_PRICE_PARAMETER_NAME)).thenReturn(MAX_PRICE_PARAMETER_VALUE);
        when(request.getParameter(MIN_PRICE_PARAMETER_NAME)).thenReturn(MIN_PRICE_PARAMETER_VALUE);
        String actualResult = filter.performConditionQuery(StringUtils.EMPTY, request);
        String expectedResult = "WHERE column BETWEEN " + MIN_PRICE_PARAMETER_VALUE + " AND " + MAX_PRICE_PARAMETER_VALUE + " ";
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowAnApplicationExceptionWhenMethodCalledWithUnParsableParameters() {
        when(request.getParameter(MAX_PRICE_PARAMETER_NAME)).thenReturn(MAX_PRICE_PARAMETER_VALUE);
        when(request.getParameter(MIN_PRICE_PARAMETER_NAME)).thenReturn(NOT_PARSABLE_PRICE_PARAMETER_VALUE);
        filter.performConditionQuery("", request);
    }
}