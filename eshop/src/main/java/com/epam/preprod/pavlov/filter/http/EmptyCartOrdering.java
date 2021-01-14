package com.epam.preprod.pavlov.filter.http;

import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.util.ProductCartUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebFilter("/EmptyCartOrdering")
public class EmptyCartOrdering extends AbstractFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Map<Product, Integer> productMap = ProductCartUtil.getProductCartFromCookie(request);
        if (Objects.isNull(productMap) || productMap.isEmpty()) {
            response.sendRedirect(request.getContextPath()+PathConstants.PRODUCT_SERVLET);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
