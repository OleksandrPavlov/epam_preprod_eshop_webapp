package com.epam.preprod.pavlov.entity.order;

public class OrderDetail {
    private int orderDetailId;
    private String detailContent;

    public OrderDetail(int orderDetailId, String detailContent) {
        this.orderDetailId = orderDetailId;
        this.detailContent = detailContent;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }
}
