package com.epam.preprod.pavlov.util;

import com.epam.preprod.pavlov.constant.NotificationContextConstants;
import com.epam.preprod.pavlov.constant.ErrorMessages;
import com.epam.preprod.pavlov.constant.FormRegExp;
import com.epam.preprod.pavlov.constant.frontend.ProductPageConfiguration;
import com.epam.preprod.pavlov.constant.frontend.ProductPreferencesFieldNames;
import com.epam.preprod.pavlov.entity.LoginForm;
import com.epam.preprod.pavlov.entity.ProductPageRule;
import com.epam.preprod.pavlov.entity.RegistrationForm;
import com.epam.preprod.pavlov.util.model.NotificationContainer;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.epam.preprod.pavlov.constant.ErrorMessages.*;
import static com.epam.preprod.pavlov.constant.product.ProductErrorMessages.*;

public class FormValidator {
    private static final String INVALID_NAME = "registration.notify.badName";
    private static final String INVALID_SURNAME = "registration.notify.badSurname";
    private static final String INVALID_EMAIL = "registration.notify.badEmail";
    private static final String INVALID_PASSWORD_FIELD = "registration.notify.badPassword";
    private static final String PASSWORD_CONFIRMATION_ERROR = "registration.notify.badConfirmPassword";
    private static final String INVALID_LOGIN_FIELD = "registration.notify.badLogin";
    private static final String INVALID_LOGIN_FORM = "invalidLoginForm";
    private static final String INVALID_AVATAR = "registration.notify.badAvatar";


    private static final String IMAGE_CONTENT_TYPE = "application/octet-stream";
    private static final String IMAGE = "image";

    private static final String INVALID_PRODUCT_QUERY = "invalidRequest";
    private static final String INVALID_PRICE_VALUE = "invalidPrice";
    private static final String UNSUPPORTED_DISPLAYED_PROD_COUNT = "unsupportedDPC";


    public static void validateRegistrationForm(ResourceBundle resourceBundle, RegistrationForm form,  String notificationContainer, HttpServletRequest request) {
        if (Objects.isNull(form.getName()) || form.getName().length() == 0 || !form.getName().matches(FormRegExp.NAME_REG_EXP)) {
           String srt = resourceBundle.getString(INVALID_NAME);

            NotificationUtil.addNewMessage(notificationContainer,INVALID_NAME,srt,request);
        }
        if (Objects.isNull(form.getEmail()) || form.getEmail().length() == 0 || !form.getEmail().matches(FormRegExp.EMAIL_REG_EXP)) {
            NotificationUtil.addNewMessage(notificationContainer,INVALID_EMAIL,resourceBundle.getString(INVALID_EMAIL),request);
        }
        if (Objects.isNull(form.getPassword()) || form.getPassword().length() == 0 || !form.getPassword().matches(FormRegExp.PASSWORD_REG_EXP)) {
            NotificationUtil.addNewMessage(notificationContainer,INVALID_PASSWORD_FIELD,resourceBundle.getString(INVALID_PASSWORD_FIELD),request);
        }
        if (Objects.isNull(form.getPasswordConfirm()) || !form.getPasswordConfirm().equals(form.getPassword())) {
            NotificationUtil.addNewMessage(notificationContainer,PASSWORD_CONFIRMATION_ERROR,resourceBundle.getString(PASSWORD_CONFIRMATION_ERROR),request);
        }
        if (Objects.isNull(form.getLogin()) || form.getLogin().length() == 0) {
            NotificationUtil.addNewMessage(notificationContainer,INVALID_LOGIN_FIELD,resourceBundle.getString(INVALID_LOGIN_FIELD),request);
        }
        Part part = form.getAvatarPart();
        if (!validateImageContent(part)) {
            NotificationUtil.addNewMessage(notificationContainer,INVALID_AVATAR,resourceBundle.getString(INVALID_AVATAR),request);
        }
    }

    public static void validateLoginForm(LoginForm loginForm, HttpServletRequest request, String notificationContainer) {
        if (Objects.isNull(loginForm.getLogin()) || loginForm.getLogin().length() == 0) {
            NotificationUtil.addNewMessage(notificationContainer, INVALID_LOGIN_FORM, EMPTY_FIELDS_MESSAGE,request);
        }
        if (Objects.isNull(loginForm.getPassword()) || loginForm.getPassword().length() == 0) {
            NotificationUtil.addNewMessage(notificationContainer, INVALID_LOGIN_FORM, EMPTY_FIELDS_MESSAGE,request);
        }
    }


    public static void meaningValidateProductPageRule(ProductPageRule pageRules, HttpServletRequest request) {
        sortValidating(pageRules.getSort(), request);
        priceValidation(pageRules.getMinPrice(), pageRules.getMaxPrice(), request);
        productDisplayedValidation(pageRules.getProductDisplayed(), request);
    }

    private static void sortValidating(String sort, HttpServletRequest request) {
        if (sort.equals(ProductPreferencesFieldNames.SORT_BY_NAME_OPTION) || sort.equals(ProductPreferencesFieldNames.SORT_BY_PRICE_OPTION) || sort.equals(StringUtils.EMPTY)) {
            return;
        }
        NotificationUtil.addNewMessage(NotificationContextConstants.PRODUCT_SEARCH_NOTIFICATION_CONTAINER, INVALID_PRODUCT_QUERY, INVALID_PRODUCT_QUERY, request);
    }

    private static void priceValidation(double minPrice, double maxPrice, HttpServletRequest request) {
        if (minPrice >= maxPrice || minPrice < 0 || maxPrice < 0) {
            NotificationUtil.addNewMessage(NotificationContextConstants.PRODUCT_SEARCH_NOTIFICATION_CONTAINER, INVALID_PRICE_VALUE, INVALID_PRICE_VALUE_MESSAGE, request);
        }
    }

    private static void productDisplayedValidation(int productDisplayed,  HttpServletRequest request) {
        if (!ProductPageConfiguration.SUPPORTED_DISPLAYED_PRODUCT_COUNT.contains(productDisplayed) && productDisplayed != 0) {
            NotificationUtil.addNewMessage(NotificationContextConstants.PRODUCT_SEARCH_NOTIFICATION_CONTAINER, UNSUPPORTED_DISPLAYED_PROD_COUNT, UNSUPPORTED_DISP_PROD_COUNT_MESSAGE, request);
        }
    }

    private static boolean validateImageContent(Part avatarPart) {
        String partContentType = avatarPart.getContentType();
        return partContentType.equals(IMAGE_CONTENT_TYPE) || partContentType.startsWith(IMAGE);
    }
}
