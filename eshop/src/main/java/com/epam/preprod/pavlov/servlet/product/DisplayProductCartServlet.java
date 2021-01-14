package com.epam.preprod.pavlov.servlet.product;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.entity.cart.ProductCart;
import com.epam.preprod.pavlov.service.product.ProductService;
import com.epam.preprod.pavlov.servlet.AbstractServlet;
import com.epam.preprod.pavlov.util.NotificationUtil;
import com.epam.preprod.pavlov.util.ProductCartUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/product-cart")
public class DisplayProductCartServlet extends AbstractServlet {
    private ProductService productService;

    @Override
    public void init() {
        productService = (ProductService) getServletContext().getAttribute(ContextConstants.PRODUCT_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NotificationUtil.cleanNotificationContextImmediately(req);
        ProductCart productCart = new ProductCart(ProductCartUtil.getProductCartFromCookie(req), productService);
        req.setAttribute(RequestConstants.PRODUCT_CART, productCart);
        forwardTo(req, resp, PathConstants.PRODUCT_CART_PAGE, PathConstants.CART_FRAME);
    }

}
