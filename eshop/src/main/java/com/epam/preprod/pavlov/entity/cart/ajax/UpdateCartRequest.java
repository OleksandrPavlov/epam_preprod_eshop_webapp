package com.epam.preprod.pavlov.entity.cart.ajax;

import com.epam.preprod.pavlov.constant.product.ProductParameters;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class UpdateCartRequest {
    public static final String ADD_OPERATION = "add";
    public static final String UPDATE_OPERATION = "update";
    public static final String REMOVE_OPERATION = "remove";
    public static final String REMOVE_ALL_OPERATION = "removeAll";
    public static final List<String> OPERATIONS = Arrays.asList(ADD_OPERATION, UPDATE_OPERATION, REMOVE_OPERATION, REMOVE_ALL_OPERATION);
    private int productId;
    private String operation;
    private int count;

    public static UpdateCartRequest parseFromRequest(HttpServletRequest request, UpdateCartResult result) {
        UpdateCartRequest updateCartRequest = new UpdateCartRequest();
        updateCartRequest.productId = getInt(request.getParameter(ProductParameters.PRODUCT_ID), result);
        result.setProductId(updateCartRequest.productId);
        updateCartRequest.count = getInt(request.getParameter(ProductParameters.PRODUCT_COUNT), result);
        if (updateCartRequest.count < 1 || updateCartRequest.count > 10000) {
            result.setStatus(0);
            result.setErrorMessage("Invalid count value!");
        }
        updateCartRequest.operation = getOperation(request, result);
        return updateCartRequest;
    }

    public static int getInt(String number, UpdateCartResult result) {
        if (!NumberUtils.isParsable(number)) {
            result.setStatus(0);
            result.setErrorMessage("Invalid update cart ajax request pack");
            return 0;
        }
        return NumberUtils.toInt(number);
    }

    private static String getOperation(HttpServletRequest request, UpdateCartResult result) {
        String operation = request.getParameter(ProductParameters.CART_OPERATION);
        if (!OPERATIONS.contains(operation)) {
            result.setStatus(0);
            result.setErrorMessage("Unsupported update operation!");
            return null;
        }
        return operation;
    }

    public int getProductId() {
        return productId;
    }

    public String getOperation() {
        return operation;
    }

    public int getCount() {
        return count;
    }
}
