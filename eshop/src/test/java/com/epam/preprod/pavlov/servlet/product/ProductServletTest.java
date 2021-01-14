package com.epam.preprod.pavlov.servlet.product;

import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.constant.frontend.ProductPageConfiguration;
import com.epam.preprod.pavlov.constant.frontend.ProductPreferencesFieldNames;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.ProductPageRule;
import com.epam.preprod.pavlov.service.product.ProductService;
import com.epam.preprod.pavlov.util.model.NotificationContainer;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServletTest {
    private static final String CATEGORY_RULE = "category";
    private static final String PRODUCER_RULE = "producer";
    private static final String SORT_RULE = "sortByPrice";
    private static final String DISPLAYED_RULE = "12";
    private static final String PRODUCT_NAME_RULE = "product";
    private static final String MAX_PRICE_RULE = "29";
    private static final String MIN_PRICE_RULE = "11";
    private static final String PAGE_NUMBER_RULE = "2";


    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductService productService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ProductServlet servlet;
    private ProductPageRule sessionProductPageRule;
    private ProductPageRule sessionWithAnotherFilterPart;

    @Before
    public void before() {
        sessionProductPageRule = new ProductPageRule();
        sessionProductPageRule.setMaxPrice(NumberUtils.toDouble(MAX_PRICE_RULE));
        sessionProductPageRule.setCurrentPage(NumberUtils.toInt(PAGE_NUMBER_RULE));
        sessionProductPageRule.setProductDisplayed(NumberUtils.toInt(DISPLAYED_RULE));
        sessionProductPageRule.setMinPrice(NumberUtils.toDouble(MIN_PRICE_RULE));
        sessionProductPageRule.setCategory(CATEGORY_RULE);
        sessionProductPageRule.setProducer(PRODUCER_RULE);
        sessionProductPageRule.setProductName(PRODUCT_NAME_RULE);
        sessionProductPageRule.setSort(SORT_RULE);

        sessionWithAnotherFilterPart = new ProductPageRule();
        sessionWithAnotherFilterPart.setMaxPrice(NumberUtils.toDouble(MAX_PRICE_RULE));
        sessionWithAnotherFilterPart.setCurrentPage(NumberUtils.toInt(PAGE_NUMBER_RULE));
        sessionWithAnotherFilterPart.setProductDisplayed(NumberUtils.toInt(DISPLAYED_RULE));
        sessionWithAnotherFilterPart.setMinPrice(NumberUtils.toDouble(MIN_PRICE_RULE));
        sessionWithAnotherFilterPart.setCategory("");
        sessionWithAnotherFilterPart.setProducer(PRODUCER_RULE);
        sessionWithAnotherFilterPart.setProductName(PRODUCT_NAME_RULE);


    }

    @Test
    public void shouldSetDefaultRuleToRequestWhenDoGetMethodCalledFirstTime() throws ServletException, IOException {
        List<Product> productsFromDB = new ArrayList<>();
        when(request.getSession()).thenReturn(session);
        when(request.getQueryString()).thenReturn(null);
        when(productService.getAllProducts(0, ProductPageConfiguration.DEFAULT_PRODUCT_COUNT)).thenReturn(productsFromDB);
        doNothing().when(request).setAttribute(RequestConstants.CURRENT_PRODUCTS, productsFromDB);
        doNothing().when(request).setAttribute("currentPage", PathConstants.PRODUCT_LIST_PAGE);
        when(request.getRequestDispatcher(PathConstants.PRODUCT_LIST_FRAME)).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void shouldSetRulesToDefaultWhenFilterPartOfRuleIsDifferent() throws ServletException, IOException {
        List<Product> products = new ArrayList<>();
        when(request.getSession()).thenReturn(session);
        expectingValidRules();
        when(request.getQueryString()).thenReturn("query");
        when(session.getAttribute(SessionConstants.CURRENT_PRODUCT_PAGE_RULES)).thenReturn(sessionWithAnotherFilterPart);
        when(productService.getPreferredProducts(request, 0, 12)).thenReturn(products);
        when(session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT)).thenReturn(new HashMap<String, NotificationContainer>());
        sessionProductPageRule.setCurrentPage(1);
        sessionProductPageRule.setPageCount(0);
        doNothing().when(session).setAttribute(SessionConstants.CURRENT_PRODUCT_PAGE_RULES, sessionProductPageRule);
        doNothing().when(request).setAttribute("currentPage", PathConstants.PRODUCT_LIST_PAGE);
        when(request.getRequestDispatcher(PathConstants.PRODUCT_LIST_FRAME)).thenReturn(requestDispatcher);
        servlet.doGet(request, response);

        verify(session).setAttribute(SessionConstants.CURRENT_PRODUCT_PAGE_RULES, sessionProductPageRule);
    }


    private void expectingValidRules() {
        when(request.getParameter(ProductPreferencesFieldNames.PRODUCT_CATEGORY)).thenReturn(CATEGORY_RULE);
        when(request.getParameter(ProductPreferencesFieldNames.PRODUCT_PRODUCER)).thenReturn(PRODUCER_RULE);
        when(request.getParameter(ProductPreferencesFieldNames.PRODUCT_MAX_PRICE)).thenReturn(MAX_PRICE_RULE);
        when(request.getParameter(ProductPreferencesFieldNames.PRODUCT_MIN_PRICE)).thenReturn(MIN_PRICE_RULE);
        when(request.getParameter(ProductPreferencesFieldNames.PRODUCT_PAGE_NUMBER)).thenReturn(PAGE_NUMBER_RULE);
        when(request.getParameter(ProductPreferencesFieldNames.PRODUCT_COUNT_FILTER)).thenReturn(DISPLAYED_RULE);
        when(request.getParameter(ProductPreferencesFieldNames.SORT_FILTER)).thenReturn(SORT_RULE);
        when(request.getParameter(ProductPreferencesFieldNames.PRODUCT_NAME)).thenReturn(PRODUCT_NAME_RULE);
    }


}