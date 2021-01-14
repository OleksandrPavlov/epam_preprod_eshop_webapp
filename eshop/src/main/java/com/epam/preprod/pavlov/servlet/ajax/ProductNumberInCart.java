package com.epam.preprod.pavlov.servlet.ajax;

import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.servlet.AbstractServlet;
import com.epam.preprod.pavlov.util.ProductCartUtil;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet("/ajax/cart/product-count")
public class ProductNumberInCart extends AbstractServlet {
    public static final String PRODUCT_CART_ITEM_COUNT = "productCartItemCount";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<Product, Integer> productCart = ProductCartUtil.getProductCartFromCookie(req);
        JSONObject jsonObject = getProductCartCountJSON(productCart);
        resp.setContentType("json/html");
        resp.getWriter().write(jsonObject.toString());
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    private JSONObject getProductCartCountJSON(Map<Product, Integer> productCart) {
        JSONObject resultJSON = new JSONObject();
        if (Objects.isNull(productCart)) {
            resultJSON.append(PRODUCT_CART_ITEM_COUNT, 0);
        } else {
            resultJSON.append(PRODUCT_CART_ITEM_COUNT, productCart.size());
        }
        return resultJSON;
    }
}
