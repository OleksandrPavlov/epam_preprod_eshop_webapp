package com.epam.preprod.pavlov.servlet.product;


import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.ProductPageRule;
import com.epam.preprod.pavlov.service.product.ProductService;
import com.epam.preprod.pavlov.servlet.AbstractServlet;
import com.epam.preprod.pavlov.util.FormValidator;
import com.epam.preprod.pavlov.util.NotificationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.epam.preprod.pavlov.constant.ContextConstants.PRODUCT_SERVICE;
import static com.epam.preprod.pavlov.constant.NotificationContextConstants.PRODUCT_SEARCH_NOTIFICATION_CONTAINER;
import static com.epam.preprod.pavlov.constant.PathConstants.PRODUCT_LIST_FRAME;
import static com.epam.preprod.pavlov.constant.PathConstants.PRODUCT_LIST_PAGE;
import static com.epam.preprod.pavlov.constant.RequestConstants.CURRENT_PRODUCTS;
import static com.epam.preprod.pavlov.constant.SessionConstants.CURRENT_PRODUCT_PAGE_RULES;
import static com.epam.preprod.pavlov.constant.frontend.ProductPageConfiguration.*;

@WebServlet("/products")
public class ProductServlet extends AbstractServlet {
    private ProductService productService;


    @Override
    public void init() throws ServletException {
        super.init();
        productService = (ProductService) getServletContext().getAttribute(PRODUCT_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        NotificationUtil.cleanNotificationContext(req);
        if (Objects.isNull(req.getQueryString())) {
            setOriginConfiguration(req, session);
        } else {
            setClientConfigurations(req);
        }
        forwardTo(req, resp, PRODUCT_LIST_PAGE, PRODUCT_LIST_FRAME);
    }


    private void setOriginConfiguration(HttpServletRequest request, HttpSession session) {
        List<Product> products = productService.getAllProducts(0, DEFAULT_PRODUCT_COUNT);
        request.setAttribute(CURRENT_PRODUCTS, products);
        ProductPageRule pageConfiguration = new ProductPageRule();
        pageConfiguration.setCurrentPage(1);
        pageConfiguration.setProductDisplayed(DEFAULT_PRODUCT_COUNT);
        pageConfiguration.setPageCount(getPageCount(productService.getProductCount(), DEFAULT_PRODUCT_COUNT));
        pageConfiguration.setMaxPrice(DEFAULT_PRODUCT_MAX_PRICE);
        pageConfiguration.setMinPrice(DEFAULT_PRODUCT_MIN_PRICE);
        session.setAttribute(CURRENT_PRODUCT_PAGE_RULES, pageConfiguration);
    }

    private int getPageCount(int itemCount, int preferredCount) {
        if (itemCount <= preferredCount || preferredCount <= 0) {
            return 0;
        }
        int slice = itemCount % preferredCount;
        int realPageCount = itemCount / DEFAULT_PRODUCT_COUNT;
        if (slice > 0) {
            realPageCount++;
        }
        return realPageCount;
    }

    private int calculateOffset(int currentPage, int limit) {
        if (currentPage <= 1) {
            return 0;
        }
        return (currentPage - 1) * limit;
    }

    private void setClientConfigurations(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ProductPageRule pageRules;
        pageRules = ProductPageRule.parseFromRequest(request);
        FormValidator.meaningValidateProductPageRule(pageRules, request);
        if (NotificationUtil.isNotificationContainerEmpty(PRODUCT_SEARCH_NOTIFICATION_CONTAINER, request)) {
            ProductPageRule sessionPageRule = (ProductPageRule) session.getAttribute(CURRENT_PRODUCT_PAGE_RULES);
            int offset = ProductPageRule.isDifferentFilterPart(pageRules, sessionPageRule) ? 0 : calculateOffset(pageRules.getCurrentPage(), pageRules.getProductDisplayed());
            if (offset == 0) {
                pageRules.setCurrentPage(1);
            }
            List<Product> products = productService.getPreferredProducts(request, offset, pageRules.getProductDisplayed());
            pageRules.setPageCount(getPageCount(productService.getProductCount(request), pageRules.getProductDisplayed()));
            request.setAttribute(CURRENT_PRODUCTS, products);
            session.setAttribute(CURRENT_PRODUCT_PAGE_RULES, pageRules);
        } else {
            setOriginConfiguration(request, session);
        }
    }

}
