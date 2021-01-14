package com.epam.preprod.pavlov.util;

import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.cart.ProductCart;
import com.epam.preprod.pavlov.exception.ValidationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ProductCartUtil {
    private static final Logger PRODUCT_CART_UTIL_LOGGER = LoggerFactory.getLogger(ProductCartUtil.class);
    private static final String INVALID_JSON_FORMAT_MESSAGE = "Invalid json format!";
    private static final String PRODUCT_NAME_FIELD = "productName";
    private static final String PRODUCT_ID_FIELD = "productId";
    private static final String PRODUCT_PRICE_FIELD = "price";
    public static final String CART_COOKIE_NAME = "productCart";
    private static final int COOKIE_AGE = 12000;

    private ProductCartUtil() {
    }

    public static Map<Product, Integer> getProductCartFromCookie(HttpServletRequest request) {
        Cookie cartCookie = getProductCartCookie(request);
        if (Objects.isNull(cartCookie)) {
            return null;
        }
        JSONObject jsonProductCart ;
        try {
            jsonProductCart = new JSONObject(URLDecoder.decode(cartCookie.getValue(), "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            PRODUCT_CART_UTIL_LOGGER.error("Invalid json format from cookies!");
            throw new ValidationException(INVALID_JSON_FORMAT_MESSAGE);
        }
        return createProductCartOfJson(jsonProductCart);
    }

    public static void putProductCartToCookie(ProductCart cart, HttpServletResponse response) {
        Map<Product, Integer> productMap = cart.getProductMap();
        JSONObject resultJson = new JSONObject();
        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
            JSONObject productJson = new JSONObject();
            productJson.put(PRODUCT_NAME_FIELD, entry.getKey().getProductName());
            productJson.put(PRODUCT_PRICE_FIELD, entry.getKey().getPrice());
            productJson.put(PRODUCT_ID_FIELD, entry.getKey().getProductId());
            resultJson.put(productJson.toString(), entry.getValue());
        }
        Cookie productCartCookie = null;
        try {
            productCartCookie = new Cookie(CART_COOKIE_NAME, URLEncoder.encode(resultJson.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            PRODUCT_CART_UTIL_LOGGER.error(INVALID_JSON_FORMAT_MESSAGE);
            throw new ValidationException(INVALID_JSON_FORMAT_MESSAGE);
        }
        productCartCookie.setMaxAge(COOKIE_AGE);
        productCartCookie.setPath("/");
        response.addCookie(productCartCookie);
    }

    public static void removeCartFromCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(CART_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static Map<Product, Integer> createProductCartOfJson(JSONObject jsonCart) {
        Map<String, Object> cartJsonMap = jsonCart.toMap();
        Map<Product, Integer> resultMap = new HashMap<>();
        cartJsonMap.forEach((key, value) -> {
            JSONObject productJson = new JSONObject(key);
            Product product = productFromJSON(productJson);
            resultMap.put(product, (Integer) value);
        });
        return resultMap;
    }

    public static Cookie getProductCartCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CART_COOKIE_NAME)) {
                return cookie;
            }
        }
        return null;
    }

    public static Product productFromJSON(JSONObject jsonProduct) {
        Product product = new Product();
        product.setProductName((String) jsonProduct.get(PRODUCT_NAME_FIELD));
        product.setPrice(jsonProduct.getDouble(PRODUCT_PRICE_FIELD));
        product.setProductId(jsonProduct.getInt(PRODUCT_ID_FIELD));
        return product;
    }

}
