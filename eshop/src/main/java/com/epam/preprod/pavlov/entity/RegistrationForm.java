package com.epam.preprod.pavlov.entity;

import com.epam.preprod.pavlov.constant.ErrorMessages;
import com.epam.preprod.pavlov.constant.FormParameters;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.util.NotificationUtil;
import com.epam.preprod.pavlov.util.model.NotificationContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static com.epam.preprod.pavlov.constant.ErrorMessages.*;
import static com.epam.preprod.pavlov.constant.FormParameters.*;
import static com.epam.preprod.pavlov.constant.RequestConstants.*;


public class RegistrationForm {
    private String login;
    private String name;
    private String surname;
    private String password;
    private String passwordConfirm;
    private String email;
    private boolean toNotify;
    private Part avatarPart;
    private String avatarName;

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public boolean isToNotify() {
        return toNotify;
    }

    public void setToNotify(boolean toNotify) {
        this.toNotify = toNotify;
    }

    public void parseRequest(HttpServletRequest request, String notificationContainer) throws IOException, ServletException {
        setName(request.getParameter(NAME_FIELD));
        setSurname(request.getParameter(SURNAME_FIELD));
        setPassword(request.getParameter(PASSWORD_FIELD));
        setEmail(request.getParameter(EMAIL_FIELD));
        setToNotify(request.getParameter(NOTIFICATION_CHECKBOX) != null);
        setPasswordConfirm(request.getParameter(PASSWORD_CONFIRM));
        try {
            setAvatarPart(request.getPart(FormParameters.AVATAR_FILE));
        } catch (IllegalStateException ex) {
            NotificationUtil.addNewMessage(notificationContainer,AVATAR_SIZE_VIOLATION,AVATAR_SIZE_VIOLATION_MESSAGE,request);
        }
        setLogin(request.getParameter(FormParameters.LOGIN_FIELD));
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Part getAvatarPart() {
        return avatarPart;
    }

    public void setAvatarPart(Part avatarPart) {
        this.avatarPart = avatarPart;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationForm that = (RegistrationForm) o;
        return toNotify == that.toNotify &&
                Objects.equals(login, that.login) &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(password, that.password) &&
                Objects.equals(passwordConfirm, that.passwordConfirm) &&
                Objects.equals(email, that.email) &&
                Objects.equals(avatarPart, that.avatarPart) &&
                Objects.equals(avatarName, that.avatarName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, name, surname, password, passwordConfirm, email, toNotify, avatarPart, avatarName);
    }
}
