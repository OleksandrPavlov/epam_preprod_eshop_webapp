package com.epam.preprod.pavlov.entity.order;

import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.sql.order.OrderConstants;
import com.epam.preprod.pavlov.constant.sql.order.OrderIdentifiers;
import com.epam.preprod.pavlov.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderPaymentType {
    private static final Map<String, Integer> supportedPayments;

    static {
        supportedPayments = new HashMap<>();
        supportedPayments.put(OrderConstants.ORDER_APPLE_PAYMENT, OrderIdentifiers.ORDER_APPLE_PAYMENT_TYPE);
        supportedPayments.put(OrderConstants.ORDER_CARD_PAYMENT, OrderIdentifiers.ORDER_CARD_PAYMENT_TYPE);
    }

    private int orderPaymentTypeId;
    private String orderPaymentTypeName;

    public OrderPaymentType(int orderPaymentTypeId, String orderPaymentTypeName) {
        this.orderPaymentTypeId = orderPaymentTypeId;
        this.orderPaymentTypeName = orderPaymentTypeName;
    }

    public int getOrderPaymentTypeId() {
        return orderPaymentTypeId;
    }

    public static OrderPaymentType parseFromRequest(HttpServletRequest request) {
        String paymentType = request.getParameter(RequestConstants.ORDER_PAYMENT);
        Integer paymentId = supportedPayments.get(paymentType);
        if (paymentType.isEmpty() || Objects.isNull(paymentId)) {
            throw new ValidationException("Invalid payment type!");
        }
        return new OrderPaymentType(paymentId, paymentType);
    }

    public void setOrderPaymentTypeId(int orderPaymentTypeId) {
        this.orderPaymentTypeId = orderPaymentTypeId;
    }

    public String getOrderPaymentTypeName() {
        return orderPaymentTypeName;
    }

    public void setOrderPaymentTypeName(String orderPaymentTypeName) {
        this.orderPaymentTypeName = orderPaymentTypeName;
    }
}
