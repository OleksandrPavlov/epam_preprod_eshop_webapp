package com.epam.preprod.pavlov.filter.service.impl;

import com.epam.preprod.pavlov.constant.frontend.ProductPreferencesFieldNames;
import com.epam.preprod.pavlov.constant.sql.SqlProductConstants;
import com.epam.preprod.pavlov.filter.container.impl.ProductSelectorFilterChain;
import com.epam.preprod.pavlov.filter.link.impl.PhraseCondition;
import com.epam.preprod.pavlov.filter.link.impl.PriceCondition;
import com.epam.preprod.pavlov.filter.service.QueryGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class MySqlQueryConditionGenerator implements QueryGenerator {
    private static final Logger MYSQL_QUERY_CONDITION_GENERATOR = LoggerFactory.getLogger(MySqlQueryConditionGenerator.class);
    private static final String BASIC_PRODUCT_SELECT_QUERY_PART = "SELECT pr.Product_ID, pr.Product_Name, pr_cat.Category_Name, pr.Product_Price, pr.Product_Count, pr_prod.Producer_Name,pr_des.Description_Body" +
            " FROM Products as pr INNER JOIN Product_Categories as pr_cat ON pr.Category_ID=pr_cat.Category_ID" +
            " INNER JOIN Product_Descriptions as pr_des ON pr.Product_ID=pr_des.Product_ID" +
            " INNER JOIN Product_Producers as pr_prod ON pr.Producer_ID=pr_prod.Producer_ID ";
    private static final String PRODUCT_COUNT = "SELECT COUNT(*) FROM Products as pr INNER JOIN Product_Categories as pr_cat ON pr.Category_ID=pr_cat.Category_ID" +
            " INNER JOIN Product_Descriptions as pr_des ON pr.Product_ID=pr_des.Product_ID" +
            " INNER JOIN Product_Producers as pr_prod ON pr.Producer_ID=pr_prod.Producer_ID ";

    private ProductSelectorFilterChain productSelectorFilterChain;

    @Override
    public void init() {
        initMySqlSelectConditionTr();
    }

    private void initMySqlSelectConditionTr() {
        productSelectorFilterChain = new ProductSelectorFilterChain();
        productSelectorFilterChain.addLink(new PhraseCondition(SqlProductConstants.PRODUCT_PRODUCER, ProductPreferencesFieldNames.PRODUCT_PRODUCER));
        productSelectorFilterChain.addLink(new PhraseCondition(SqlProductConstants.PRODUCT_CATEGORY, ProductPreferencesFieldNames.PRODUCT_CATEGORY));
        productSelectorFilterChain.addLink(new PhraseCondition(SqlProductConstants.PRODUCT_NAME, ProductPreferencesFieldNames.PRODUCT_NAME));
        productSelectorFilterChain.addLink(new PriceCondition(ProductPreferencesFieldNames.PRODUCT_MAX_PRICE, ProductPreferencesFieldNames.PRODUCT_MIN_PRICE, SqlProductConstants.PRODUCT_PRICE));
        MYSQL_QUERY_CONDITION_GENERATOR.debug("Query generator has been initialized");
    }

    @Override
    public String getProductSelectQueryByUrl(HttpServletRequest request) {
        return BASIC_PRODUCT_SELECT_QUERY_PART + productSelectorFilterChain.doChain(request);
    }

    public String getProductCountQueryByUrl(HttpServletRequest request) {
        return PRODUCT_COUNT + productSelectorFilterChain.doChain(request);
    }
}
