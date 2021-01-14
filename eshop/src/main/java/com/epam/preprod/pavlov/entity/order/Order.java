package com.epam.preprod.pavlov.entity.order;

import com.epam.preprod.pavlov.entity.Product;

import java.time.LocalDateTime;
import java.util.Map;

public class Order {
    private int orderId;
    private LocalDateTime creationTime;
    private BriefOrderUserInfo userInfo;
    private OrderDetail orderDetail;
    private OrderPaymentType paymentType;
    private OrderStatus orderStatus;
    private Map<Product, Integer> productMap;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public BriefOrderUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BriefOrderUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public OrderPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(OrderPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Map<Product, Integer> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<Product, Integer> productMap) {
        this.productMap = productMap;
    }
}
