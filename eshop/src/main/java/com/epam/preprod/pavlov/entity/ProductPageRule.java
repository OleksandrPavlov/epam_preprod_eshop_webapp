package com.epam.preprod.pavlov.entity;

import com.epam.preprod.pavlov.constant.frontend.ProductPreferencesFieldNames;
import com.epam.preprod.pavlov.exception.ValidationException;
import com.epam.preprod.pavlov.util.NotificationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.epam.preprod.pavlov.constant.NotificationContextConstants.PRODUCT_SEARCH_NOTIFICATION_CONTAINER;
import static com.epam.preprod.pavlov.constant.ErrorTitles.INVALID_INPUT;

/**
 * @author Pavlov Aleksandr
 * <p>
 * This class represent all preferences user chose on product page as java object.
 */
public class ProductPageRule {
    private static final String INVALID_NUMBER_FORMAT_MESSAGE = "Sorry but you entered invalid number!";
    private String productName = StringUtils.EMPTY;
    private String category = StringUtils.EMPTY;
    private String producer = StringUtils.EMPTY;
    private double maxPrice;
    private double minPrice;
    private int currentPage;
    private int productDisplayed;
    private String sort = StringUtils.EMPTY;
    private int pageCount;

    public static ProductPageRule parseFromRequest(HttpServletRequest request)  {
        ProductPageRule productPageConfig = new ProductPageRule();
        try {
            productPageConfig.category = setStringValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_CATEGORY, request);
            productPageConfig.producer = setStringValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_PRODUCER, request);
            productPageConfig.maxPrice = setDoubleValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_MAX_PRICE, request);
            productPageConfig.minPrice = setDoubleValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_MIN_PRICE, request);
            productPageConfig.currentPage = setIntegerValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_PAGE_NUMBER, request);
            productPageConfig.productDisplayed = setIntegerValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_COUNT_FILTER, request);
            productPageConfig.sort = setStringValueOrEmpty(ProductPreferencesFieldNames.SORT_FILTER, request);
            productPageConfig.productName = setStringValueOrEmpty(ProductPreferencesFieldNames.PRODUCT_NAME, request);
        }catch(ValidationException ex){
            NotificationUtil.addNewMessage(PRODUCT_SEARCH_NOTIFICATION_CONTAINER,INVALID_INPUT, ExceptionUtils.getMessage(ex),request);
        }
        return productPageConfig;
    }

    private static String setStringValueOrEmpty(String parameterName, HttpServletRequest request) {
        String currentValue = request.getParameter(parameterName);
        return StringUtils.isBlank(currentValue) ? StringUtils.EMPTY : currentValue;
    }

    private static double setDoubleValueOrEmpty(String parameterName, HttpServletRequest request) throws ValidationException {
        String currentValue = request.getParameter(parameterName);
        if (StringUtils.isBlank(currentValue)) {
            return 0;
        }
        if (!NumberUtils.isParsable(currentValue)) {
            throw new ValidationException(INVALID_NUMBER_FORMAT_MESSAGE);
        }
        return Double.parseDouble(currentValue);
    }

    private static int setIntegerValueOrEmpty(String parameterName, HttpServletRequest request) throws ValidationException {
        String currentValue = request.getParameter(parameterName);
        if (StringUtils.isBlank(currentValue)) {
            return 0;
        }
        if (!NumberUtils.isParsable(currentValue)) {
            throw new ValidationException(INVALID_NUMBER_FORMAT_MESSAGE);
        }
        return NumberUtils.toInt(currentValue);
    }

    public static boolean isDifferentFilterPart(ProductPageRule rulesA, ProductPageRule rulesB) {
        return !(Objects.nonNull(rulesA)
                && Objects.nonNull(rulesB)
                && rulesA.category.equals(rulesB.category)
                && rulesA.producer.equals(rulesB.producer)
                && rulesA.productName.equals(rulesB.productName)
                && rulesA.maxPrice == rulesB.maxPrice
                && rulesA.minPrice == rulesB.minPrice
                && rulesA.getSort().equals(rulesB.getSort())
                && rulesA.getProductDisplayed() == rulesB.getProductDisplayed());
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

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getProductDisplayed() {
        return productDisplayed;
    }

    public void setProductDisplayed(int productDisplayed) {
        this.productDisplayed = productDisplayed;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductPageRule pageRule = (ProductPageRule) o;
        return Double.compare(pageRule.maxPrice, maxPrice) == 0 &&
                Double.compare(pageRule.minPrice, minPrice) == 0 &&
                currentPage == pageRule.currentPage &&
                productDisplayed == pageRule.productDisplayed &&
                pageCount == pageRule.pageCount &&
                Objects.equals(productName, pageRule.productName) &&
                Objects.equals(category, pageRule.category) &&
                Objects.equals(producer, pageRule.producer) &&
                Objects.equals(sort, pageRule.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, category, producer, maxPrice, minPrice, currentPage, productDisplayed, sort, pageCount);
    }
}
