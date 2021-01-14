package com.epam.preprod.pavlov.entity;

import com.epam.preprod.pavlov.annotation.Column;
import com.epam.preprod.pavlov.constant.sql.SqlProductConstants;

import java.util.Objects;

/**
 * @author Pavlov ALeksandr
 * This Object represents product from database as java object.
 */
public class Product {
    @Column(name = SqlProductConstants.PRODUCT_ID)
    private int productId;
    @Column(name = SqlProductConstants.PRODUCT_NAME)
    private String productName;
    @Column(name = SqlProductConstants.PRODUCT_CATEGORY)
    private String categoryName;
    @Column(name = SqlProductConstants.PRODUCT_PRODUCER)
    private String producerName;
    @Column(name = SqlProductConstants.PRODUCT_PRICE)
    private double price;
    @Column(name = SqlProductConstants.PRODUCT_DESCRIPTION)
    private String productDescription;
    @Column(name = SqlProductConstants.PRODUCT_COUNT)
    private int productCount;

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public String getProducerName() {
        return producerName;
    }

    public double getPrice() {
        return price;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductCount() {
        return productCount;
    }


    public void setProductId(int productId) {
        this.productId = productId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return productId == product.productId &&
                Double.compare(product.price, price) == 0 &&
                productCount == product.productCount &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(categoryName, product.categoryName) &&
                Objects.equals(producerName, product.producerName) &&
                Objects.equals(productDescription, product.productDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, categoryName, producerName, price, productDescription, productCount);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", producerName='" + producerName + '\'' +
                ", price=" + price +
                ", productDescription='" + productDescription + '\'' +
                ", productCount=" + productCount +
                '}';
    }
}
