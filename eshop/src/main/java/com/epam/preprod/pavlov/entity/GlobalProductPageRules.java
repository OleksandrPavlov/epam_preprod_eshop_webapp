package com.epam.preprod.pavlov.entity;

import com.epam.preprod.pavlov.constant.frontend.ProductPreferencesFieldNames;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class GlobalProductPageRules {
    private String productName="";
    private String category="";
    private String producer="";
    private String maxPrice="";
    private String minPrice="";
    private String currentPage="";
    private String productDisplayed="";
    private String sort="";

    public static GlobalProductPageRules parseFromRequest(HttpServletRequest request) {
        GlobalProductPageRules productPageConfig = new GlobalProductPageRules();
        productPageConfig.category = setValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_CATEGORY, request);
        productPageConfig.producer = setValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_PRODUCER, request);
        productPageConfig.maxPrice = setValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_MAX_PRICE, request);
        productPageConfig.minPrice = setValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_MIN_PRICE, request);
        productPageConfig.currentPage = setValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_PAGE_NUMBER, request);
        productPageConfig.productDisplayed = setValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_COUNT_FILTER, request);
        productPageConfig.sort = setValueOrEmpty(ProductPreferencesFieldNames.SORT_FILTER, request);
        productPageConfig.productName = setValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_NAME, request);
        return productPageConfig;
    }

    private static String setValueOrEmpty(String parameterName, HttpServletRequest request) {
        String currentValue = request.getParameter(parameterName);
        return currentValue == null ? StringUtils.EMPTY : currentValue;
    }

    public static boolean isDifferentFilterPart(GlobalProductPageRules rulesA, GlobalProductPageRules rulesB) {
        return !(Objects.nonNull(rulesA)
                && Objects.nonNull(rulesB)
                && rulesA.category.equals(rulesB.category)
                && rulesA.producer.equals(rulesB.producer)
                && rulesA.productName.equals(rulesB.productName)
                && rulesA.maxPrice.equals(rulesB.maxPrice)
                && rulesA.minPrice.equals(rulesB.minPrice));
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getProductDisplayed() {
        return productDisplayed;
    }

    public void setProductDisplayed(String productDisplayed) {
        this.productDisplayed = productDisplayed;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "GlobalProductPageRules{" +
                "productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", producer='" + producer + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                ", minPrice='" + minPrice + '\'' +
                ", currentPage='" + currentPage + '\'' +
                ", productDisplayed='" + productDisplayed + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }
}
