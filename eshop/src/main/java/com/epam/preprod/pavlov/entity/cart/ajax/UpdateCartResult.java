package com.epam.preprod.pavlov.entity.cart.ajax;

public class UpdateCartResult {
    private int productId;
    private String errorMessage;
    private int status = 1;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
