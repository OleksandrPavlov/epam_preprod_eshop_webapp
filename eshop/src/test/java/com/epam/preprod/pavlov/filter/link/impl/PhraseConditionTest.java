package com.epam.preprod.pavlov.filter.link.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhraseConditionTest {
    private static final String DB_COLUMN_NAME = "column";
    private static final String PARAMETER_NAME_1 = "parameter_1";
    private static final String PARAMETER_NAME_4 = "parameter_4";
    private static final String PARAMETER_VALUE_1 = "value_1";
    private static final String PARAMETER_VALUE_2 = "value_2";
    private static final String PARAMETER_VALUE_3 = "value_3";
    private static final String PARAMETER_VALUE_4 = "value_4";
    private static final String EMPTY_CURRENT_QUERY = "";
    private static final String NOT_EMPTY_CURRENT_QUERY = "WHERE column1='value1'";

    @Mock
    private HttpServletRequest request;

    private PhraseCondition filter;

    @Before
    public void init() {
        filter = new PhraseCondition(DB_COLUMN_NAME, PARAMETER_NAME_1);
    }

    @Test
    public void shouldPrepareQueryWhenMethodCalledWithEmptyCurrentResult() {
        String[] values = new String[]{PARAMETER_VALUE_1, PARAMETER_VALUE_2, PARAMETER_VALUE_3};
        when(request.getParameterValues(PARAMETER_NAME_1)).thenReturn(values);
        String resultQuery = filter.performConditionQuery(EMPTY_CURRENT_QUERY, request);
        String expectedQuery = "WHERE column='value_1' AND column='value_2' AND column='value_3' ";
        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    public void shouldReturnQueryWhenMethodCalledWithNotEmptyCurrentResult() {
        String[] values = new String[]{PARAMETER_VALUE_1, PARAMETER_VALUE_2, PARAMETER_VALUE_3};
        when(request.getParameterValues(PARAMETER_NAME_1)).thenReturn(values);
        String resultQuery = filter.performConditionQuery(NOT_EMPTY_CURRENT_QUERY, request);
        String expectedQuery = NOT_EMPTY_CURRENT_QUERY + "AND column='value_1' AND column='value_2' AND column='value_3' ";
        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    public void shouldReturnQueryWithoutChangesWhenPerformQueryMethodCalled() {
        when(request.getParameterValues(PARAMETER_NAME_1)).thenReturn(new String[]{});
        String resultQuery = filter.performConditionQuery(NOT_EMPTY_CURRENT_QUERY, request);
        assertEquals(NOT_EMPTY_CURRENT_QUERY, resultQuery);
    }

    @Test
    public void shouldReturnFalseWhenFilterUnableToServeRequest() {
        when(request.getParameter(PARAMETER_NAME_1)).thenReturn(null);
        boolean isAble = filter.ableToServe(request);
        assertFalse(isAble);
    }

    @Test
    public void shouldReturnTrueWhenFilterAbleToServeMethodCalled() {
        when(request.getParameter(PARAMETER_NAME_1)).thenReturn(PARAMETER_VALUE_1);
        boolean isAble = filter.ableToServe(request);
        assertTrue(isAble);
    }

    @Test
    public void shouldReturnCorrectQueryWhenDoFilterCalled() {
        when(request.getParameter(PARAMETER_NAME_1)).thenReturn(PARAMETER_VALUE_1);
        String[] values = new String[]{PARAMETER_VALUE_1, PARAMETER_VALUE_2, PARAMETER_VALUE_3};
        when(request.getParameterValues(PARAMETER_NAME_1)).thenReturn(values);
        String expectedQuery = "WHERE column='value_1' AND column='value_2' AND column='value_3' ";
        String result = filter.doFilter(request);
        assertEquals(expectedQuery, result);
    }

    @Test
    public void shouldReturnCorrectQueryWhenDoFilterCalledOnLinkedFilter() {
        PhraseCondition phraseCondition = new PhraseCondition("column2", PARAMETER_NAME_4);
        filter.setNext(phraseCondition);
        when(request.getParameter(PARAMETER_NAME_4)).thenReturn(PARAMETER_VALUE_4);
        String[] valuesOnAnotherLink = new String[]{PARAMETER_VALUE_4};
        when(request.getParameterValues(PARAMETER_NAME_4)).thenReturn(valuesOnAnotherLink);

        String[] values = new String[]{PARAMETER_VALUE_1, PARAMETER_VALUE_2, PARAMETER_VALUE_3};
        when(request.getParameterValues(PARAMETER_NAME_1)).thenReturn(values);
        when(request.getParameter(PARAMETER_NAME_1)).thenReturn(PARAMETER_VALUE_1);

        String actualQuery = filter.doFilter(request);
        String expectedQuery = "WHERE column2='value_4' AND column='value_1' AND column='value_2' AND column='value_3' ";
        assertEquals(expectedQuery, actualQuery);
    }


}