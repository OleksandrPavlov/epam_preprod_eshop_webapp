package com.epam.preprod.pavlov.listener;


import com.epam.preprod.pavlov.constant.ApplicationInitConstants;
import com.epam.preprod.pavlov.constant.CaptchaConfigConstants;
import com.epam.preprod.pavlov.constant.frontend.ProductPreferencesFieldNames;
import com.epam.preprod.pavlov.constant.sql.SqlProductConstants;
import com.epam.preprod.pavlov.dao.OrderDao;
import com.epam.preprod.pavlov.dao.ProductDao;
import com.epam.preprod.pavlov.dao.impl.OrderDaoImpl;
import com.epam.preprod.pavlov.dao.impl.ProductDaoImpl;
import com.epam.preprod.pavlov.dao.impl.UserDaoImpl;
import com.epam.preprod.pavlov.entity.CaptchaPack;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.factory.CaptchaServantFactory;
import com.epam.preprod.pavlov.factory.impl.CaptchaServantFactoryImpl;
import com.epam.preprod.pavlov.filter.chain.FilterChain;
import com.epam.preprod.pavlov.filter.chain.impl.ProductSelectorFilterChain;
import com.epam.preprod.pavlov.filter.link.impl.MySQlSelectConditionTranslator;
import com.epam.preprod.pavlov.filter.link.impl.PhraseCondition;
import com.epam.preprod.pavlov.filter.link.impl.PriceCondition;
import com.epam.preprod.pavlov.filter.link.impl.SortCondition;
import com.epam.preprod.pavlov.handler.DefaultResultSetHandler;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.jdbc.impl.TransactionManagerImpl;
import com.epam.preprod.pavlov.service.account.AccountService;
import com.epam.preprod.pavlov.service.account.impl.AccountServiceImpl;
import com.epam.preprod.pavlov.service.additional.AvatarService;
import com.epam.preprod.pavlov.service.additional.impl.AvatarServiceImpl;
import com.epam.preprod.pavlov.service.captcha.CaptchaManager;
import com.epam.preprod.pavlov.service.captcha.CaptchaService;
import com.epam.preprod.pavlov.service.captcha.courier.CaptchaCourier;
import com.epam.preprod.pavlov.service.captcha.courier.impl.ContextCaptchaCourier;
import com.epam.preprod.pavlov.service.captcha.courier.impl.SessionCaptchaCourier;
import com.epam.preprod.pavlov.service.captcha.destroyer.CaptchaDestroyer;
import com.epam.preprod.pavlov.service.captcha.destroyer.impl.ContextCaptchaDestroyer;
import com.epam.preprod.pavlov.service.captcha.destroyer.impl.SessionCaptchaDestroyer;
import com.epam.preprod.pavlov.service.captcha.impl.CaptchaManagerImpl;
import com.epam.preprod.pavlov.service.captcha.impl.CaptchaServiceImpl;
import com.epam.preprod.pavlov.service.captcha.supplier.CaptchaSupplier;
import com.epam.preprod.pavlov.service.captcha.supplier.impl.ContextCaptchaSupplier;
import com.epam.preprod.pavlov.service.captcha.supplier.impl.SessionCaptchaSupplier;
import com.epam.preprod.pavlov.service.order.OrderService;
import com.epam.preprod.pavlov.service.order.impl.OrderServiceImpl;
import com.epam.preprod.pavlov.service.product.ProductService;
import com.epam.preprod.pavlov.service.product.impl.ProductServiceImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.epam.preprod.pavlov.constant.ContextConstants.*;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger CONTEXT_LISTENER_LOGGER
            = LoggerFactory.getLogger(AppContextListener.class);
    private static final String RELATIVE_AVATAR_FOLDER_PATH = "static/img/";
    private Properties applicationProperties;
    private BasicDataSource dataSource;
    private UserDaoImpl userDaoRepository;
    private CaptchaManager captchaManager;
    private CaptchaServantFactory captchaServantFactory;
    private CaptchaService captchaService;
    private AvatarService avatarService;
    private AccountService userService;
    private TransactionManager transactionManager;
    private ProductService productService;
    private OrderService orderService;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        initApplicationProperties();
        initBasicDataSource();
        initUserDaoRepository();
        initAvatarService(servletContextEvent);
        initUserService();
        initProductService();
        initOrderService();
        initCaptchaService();
        initCaptchaServantFactory(servletContextEvent);
        initCaptchaManager();
        context.setAttribute(AVATAR_SERVICE, avatarService);
        context.setAttribute(CAPTCHA_CONTAINER, new HashMap<String, CaptchaPack>());
        context.setAttribute(CAPTCHA_MANAGER, captchaManager);
        context.setAttribute(ACCOUNT_SERVICE, userService);
        context.setAttribute(PRODUCT_SERVICE, productService);
        context.setAttribute(ORDER_SERVICE, orderService);
        CONTEXT_LISTENER_LOGGER.debug("Context has been initialized");
        System.out.println("ServletContext=" + servletContextEvent.getServletContext().getRealPath("."));
    }


    private void initUserService() {
        transactionManager = new TransactionManagerImpl(dataSource);
        userService = new AccountServiceImpl(userDaoRepository, transactionManager);
        CONTEXT_LISTENER_LOGGER.debug("user service has been initialized");
    }


    private void initCaptchaServantFactory(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        String preferredCaptchaStoringStrategy = context.getInitParameter(CAPTCHA_STORING_STRATEGY);
        Map<String, CaptchaCourier> couriers = new HashMap<>();
        couriers.put(CONTEXT_CAPTCHA_STORING_STRATEGY, new ContextCaptchaCourier());
        couriers.put(SESSION_CAPTCHA_STORING_STRATEGY, new SessionCaptchaCourier());
        Map<String, CaptchaSupplier> suppliers = new HashMap<>();
        suppliers.put(CONTEXT_CAPTCHA_STORING_STRATEGY, new ContextCaptchaSupplier());
        suppliers.put(SESSION_CAPTCHA_STORING_STRATEGY, new SessionCaptchaSupplier());
        Map<String, CaptchaDestroyer> destroyers = new HashMap<>();
        destroyers.put(CONTEXT_CAPTCHA_STORING_STRATEGY, new ContextCaptchaDestroyer());
        destroyers.put(SESSION_CAPTCHA_STORING_STRATEGY, new SessionCaptchaDestroyer());
        captchaServantFactory = new CaptchaServantFactoryImpl(couriers, suppliers, destroyers, preferredCaptchaStoringStrategy);
        CONTEXT_LISTENER_LOGGER.debug("Captcha servant factory has been initialized");

    }

    private void initCaptchaService() {
        captchaService = new CaptchaServiceImpl((int) System.currentTimeMillis(), CaptchaConfigConstants.KEY_LENGTH);
        CONTEXT_LISTENER_LOGGER.debug("Captcha service has been initialized");

    }

    private void initCaptchaManager() {
        captchaManager = new CaptchaManagerImpl(captchaService, captchaServantFactory);
        CONTEXT_LISTENER_LOGGER.debug("Captcha manager has been initialized");
    }

    private void initUserDaoRepository() {
        DefaultResultSetHandler userResultSetHandler = new DefaultResultSetHandler(User.class, false);
        QueryRunner queryRunner = new QueryRunner();
        userDaoRepository = new UserDaoImpl(queryRunner, userResultSetHandler);
        CONTEXT_LISTENER_LOGGER.debug("User repository has been initialized");

    }

    private void initAvatarService(ServletContextEvent event) {
        String pathToAvatarFolder = applicationProperties.getProperty(ApplicationInitConstants.AVATARS_FOLDER);
        this.avatarService = new AvatarServiceImpl(pathToAvatarFolder, event.getServletContext().getRealPath(RELATIVE_AVATAR_FOLDER_PATH));
        CONTEXT_LISTENER_LOGGER.debug("Avatar service has been initialized!");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            dataSource.close();
        } catch (SQLException e) {
            CONTEXT_LISTENER_LOGGER.error("IO exception was occured during closing dataSource!");
        }
    }

    private void initBasicDataSource() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(applicationProperties.getProperty(ApplicationInitConstants.DB_DRIVER));
        dataSource.setUrl(applicationProperties.getProperty(ApplicationInitConstants.DB_URL));
        dataSource.setUsername(applicationProperties.getProperty(ApplicationInitConstants.DB_USERNAME));
        dataSource.setPassword(applicationProperties.getProperty(ApplicationInitConstants.DB_PASSWORD));
        dataSource.setInitialSize(NumberUtils.createInteger(applicationProperties.getProperty(ApplicationInitConstants.DB_INIT_POOL_SIZE)));
        dataSource.setMaxTotal(NumberUtils.createInteger(applicationProperties.getProperty(ApplicationInitConstants.DB_MAX_POOL_SIZE)));
        CONTEXT_LISTENER_LOGGER.debug("Connection pool has been initialized!");
    }

    private void initProductService() {
        ProductDao productDao = new ProductDaoImpl();
        productService = new ProductServiceImpl(transactionManager, productDao, initFilterChain());
        CONTEXT_LISTENER_LOGGER.debug("Product service has been initialized!");
    }


    private void initOrderService() {
        OrderDao orderDao = new OrderDaoImpl();
        orderService = new OrderServiceImpl(transactionManager, orderDao);
        CONTEXT_LISTENER_LOGGER.debug("Order service has been initialized!");
    }

    private FilterChain<MySQlSelectConditionTranslator, String> initFilterChain() {
        FilterChain<MySQlSelectConditionTranslator, String> productSelectorFilterChain = new ProductSelectorFilterChain();
        Map<String, String> mappingParameterOnDbColumn = new HashMap<>();
        mappingParameterOnDbColumn.put(ProductPreferencesFieldNames.SORT_BY_NAME_OPTION, SqlProductConstants.PRODUCT_NAME);
        mappingParameterOnDbColumn.put(ProductPreferencesFieldNames.SORT_BY_PRICE_OPTION, SqlProductConstants.PRODUCT_PRICE);
        productSelectorFilterChain.addLink(new SortCondition(ProductPreferencesFieldNames.SORT_FILTER, mappingParameterOnDbColumn));
        productSelectorFilterChain.addLink(new PhraseCondition(SqlProductConstants.PRODUCT_PRODUCER, ProductPreferencesFieldNames.PRODUCT_PRODUCER));
        productSelectorFilterChain.addLink(new PhraseCondition(SqlProductConstants.PRODUCT_CATEGORY, ProductPreferencesFieldNames.PRODUCT_CATEGORY));
        productSelectorFilterChain.addLink(new PhraseCondition(SqlProductConstants.PRODUCT_NAME, ProductPreferencesFieldNames.PRODUCT_NAME));
        productSelectorFilterChain.addLink(new PriceCondition(ProductPreferencesFieldNames.PRODUCT_MAX_PRICE, ProductPreferencesFieldNames.PRODUCT_MIN_PRICE, SqlProductConstants.PRODUCT_PRICE));
        return productSelectorFilterChain;
    }

    private void initApplicationProperties() {
        applicationProperties = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(ApplicationInitConstants.APPLICATION_PROPERTIES_FILE)) {
            applicationProperties.load(inputStream);
        } catch (IOException e) {
            CONTEXT_LISTENER_LOGGER.error("IO exception was occurred during loading application properties");
        }
        CONTEXT_LISTENER_LOGGER.debug("Application properties object has been initialized!");
    }


}
