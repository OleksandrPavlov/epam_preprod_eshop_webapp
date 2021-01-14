package com.epam.preprod.pavlov.constant;

public class ErrorMessages {
    private ErrorMessages() {

    }

    public static final String INVALID_NAME = "Your name is not valid!";
    public static final String INVALID_EMAIL = "Your email is not valid!";
    public static final String INVALID_PASSWORD = "Your password is not valid!";
    public static final String EMPTY_FIELD = "You have empty fields!";
    public static final String USER_ALREADY_EXISTS = "User with same data already exist!";
    public static final String CAPTCHA_EXPIRED = "Sorry but validation process took a lot of time!";
    public static final String CAPTCHA_NOT_VALID = "Sorry but you entered wrong captcha code!";
    public static final String PASSWORD_CONFIRMATION = "Different passwords!";
    public static final String AVATAR_SIZE_VIOLATION_MESSAGE = "Avatar has unacceptable size!";
    public static final String AVATAR_EXTENSION_VIOLATION_MESSAGE = "Invalid avatar!";
    public static final String EMPTY_FIELDS_MESSAGE = "There is empty field!";
    public static final String LOGIN_ALREADY_EXISTS = "The same login already exists!";
    public static final String INVALID_LOGIN_MESSAGE = "Incorrect login!";
    public static final String SQL_EXCEPTION_BODY = "sqlexception";
    public static final String CLIENT_INFO_ERROR_MESSAGE = "ClientInfoException has been happened during attempt of writing exception notification";
    public static final String SERVICE_ERROR_NOTIFICATION_CONTAINER = "serviceNotificationContainer";
    public static final String ACCOUNT_ERROR_NOTIFICATION_CONTAINER = "accountNotificationContainer";
    public static final String ORDER_ERROR_NOTIFICATION_CONTAINER = "orderNotificationContainer";


}
