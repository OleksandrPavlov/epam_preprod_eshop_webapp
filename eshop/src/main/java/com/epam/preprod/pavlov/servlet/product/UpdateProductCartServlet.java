package com.epam.preprod.pavlov.servlet.product;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.cart.ProductCart;
import com.epam.preprod.pavlov.entity.cart.ajax.UpdateCartRequest;
import com.epam.preprod.pavlov.entity.cart.ajax.UpdateCartResult;
import com.epam.preprod.pavlov.service.product.ProductService;
import com.epam.preprod.pavlov.servlet.AbstractServlet;
import com.epam.preprod.pavlov.util.ProductCartUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet("/ajax/cart/update-cart")
public class UpdateProductCartServlet extends AbstractServlet {
    private static final String ITEM_COUNT = "itemCount";
    private static final String TOTAL_PRICE = "totalPrice";
    private ProductService productService;

    @Override
    public void init() {
        productService = (ProductService) getServletContext().getAttribute(ContextConstants.PRODUCT_SERVICE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UpdateCartResult updateCartResult = new UpdateCartResult();
        UpdateCartRequest updateCartRequest = UpdateCartRequest.parseFromRequest(req, updateCartResult);
        ProductCart productCart = null;
        if (updateCartResult.getStatus() != 0) {
            Map<Product, Integer> productMap = ProductCartUtil.getProductCartFromCookie(req);
            productCart = Objects.isNull(productMap) ? new ProductCart(productService) : new ProductCart(productMap, productService);
            productCart.updateProductCart(updateCartRequest);
            ProductCartUtil.putProductCartToCookie(productCart, resp);
        }
        sendResult(resp, updateCartResult, productCart);
    }

    private JSONObject getJSONResult(UpdateCartResult cartResult, ProductCart productCart) {
        if (cartResult.getStatus() == 0) {
            return new JSONObject(cartResult);
        }
        JSONObject jsonResult = new JSONObject(cartResult);
        jsonResult.put(ITEM_COUNT, productCart.getProductList().size());
        jsonResult.put(TOTAL_PRICE, String.format("%.2f", productCart.calculateTotalPrice()));
        return jsonResult;
    }

    private void sendResult(HttpServletResponse response, UpdateCartResult result, ProductCart productCart) throws IOException {
        response.getWriter().write(getJSONResult(result, productCart).toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
