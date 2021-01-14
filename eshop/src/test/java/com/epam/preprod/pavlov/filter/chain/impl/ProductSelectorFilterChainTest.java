package com.epam.preprod.pavlov.filter.chain.impl;

import com.epam.preprod.pavlov.filter.chain.FilterChain;
import com.epam.preprod.pavlov.filter.link.impl.MySQlSelectConditionTranslator;
import com.epam.preprod.pavlov.filter.link.impl.PhraseCondition;
import com.epam.preprod.pavlov.filter.link.impl.PriceCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class ProductSelectorFilterChainTest {
    public static final String MAX_PRICE_PARAMETER_NAME = "maxPrice";
    public static final String MIN_PRICE_PARAMETER_NAME = "minPrice";
    public static final String MIN_PRICE_PARAMETER_VALUE = "16.0";
    public static final String MAX_PRICE_PARAMETER_VALUE = "23.0";
    public static final String NAME_PARAMETER_VALUE = "name";
    public static final String NAME_PARAMETER = "nameParameter";
    public static final String PRICE_COLUMN = "priceColumn";
    public static final String NAME_COLUMN = "nameColumn";
    @Mock
    private HttpServletRequest request;

    @Test
    public void doChain() {
        FilterChain<MySQlSelectConditionTranslator, String> filterChain = new ProductSelectorFilterChain();
        PriceCondition priceFilter = new PriceCondition(MAX_PRICE_PARAMETER_NAME, MIN_PRICE_PARAMETER_NAME, PRICE_COLUMN);
        PhraseCondition nameFilter = new PhraseCondition(NAME_COLUMN, NAME_PARAMETER);
        filterChain.addLink(priceFilter);
        filterChain.addLink(nameFilter);

        when(request.getParameter(NAME_PARAMETER)).thenReturn(NAME_PARAMETER_VALUE);
        when(request.getParameterValues(NAME_PARAMETER)).thenReturn(new String[]{NAME_PARAMETER_VALUE});
        when(request.getParameter(MAX_PRICE_PARAMETER_NAME)).thenReturn(MAX_PRICE_PARAMETER_VALUE);
        when(request.getParameter(MIN_PRICE_PARAMETER_NAME)).thenReturn(MIN_PRICE_PARAMETER_VALUE);

        String result = filterChain.doChain(request);
        String expectedResult = "WHERE nameColumn='name' AND priceColumn BETWEEN 16.0 AND 23.0 ";
        assertEquals(result, expectedResult);
        verify(request, times(2)).getParameter(MAX_PRICE_PARAMETER_NAME);
        verify(request, times(2)).getParameter(MIN_PRICE_PARAMETER_NAME);
    }
}