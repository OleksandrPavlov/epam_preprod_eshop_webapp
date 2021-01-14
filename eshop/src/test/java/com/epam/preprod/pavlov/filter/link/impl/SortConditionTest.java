package com.epam.preprod.pavlov.filter.link.impl;

import com.epam.preprod.pavlov.constant.frontend.ProductPreferencesFieldNames;
import com.epam.preprod.pavlov.constant.sql.SqlProductConstants;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SortConditionTest {
    private static Map<String, String> parameterColumn;
    
    @Mock
    private HttpServletRequest request;

    @BeforeClass
    public static void beforeClass() {
        parameterColumn = new HashMap<>();
        parameterColumn.put(ProductPreferencesFieldNames.SORT_BY_NAME_OPTION, SqlProductConstants.PRODUCT_NAME);
        parameterColumn.put(ProductPreferencesFieldNames.SORT_BY_PRICE_OPTION, SqlProductConstants.PRODUCT_PRICE);
    }

    @Test
    public void performConditionQuery() {
        SortCondition filter = new SortCondition(ProductPreferencesFieldNames.SORT_FILTER, parameterColumn);
        when(request.getParameter(ProductPreferencesFieldNames.SORT_FILTER)).thenReturn(ProductPreferencesFieldNames.SORT_BY_NAME_OPTION);
        String expectedQuery = "ORDER BY Product_Name ";
        String actualQuery = filter.performConditionQuery("", request);
        assertEquals(expectedQuery, actualQuery);
    }


}