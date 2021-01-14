package com.epam.preprod.pavlov.constant;

public class FormRegExp {
    private FormRegExp() {

    }

    public static final String NAME_REG_EXP = "^[a-zA-Zа-яА-Я]+$";
    public static final String EMAIL_REG_EXP = "^[0-9a-z-\\.]+\\@[0-9a-z-]{2,}\\.[a-z]{2,}$";
    public static final String PASSWORD_REG_EXP = "^[a-zA-z]{6,}$";
}
