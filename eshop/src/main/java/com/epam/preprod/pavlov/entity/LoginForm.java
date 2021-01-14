package com.epam.preprod.pavlov.entity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

import static com.epam.preprod.pavlov.constant.FormParameters.LOGIN_FIELD;
import static com.epam.preprod.pavlov.constant.FormParameters.PASSWORD_FIELD;
import static com.epam.preprod.pavlov.constant.FormParameters.REMEMBER_ME;


public class LoginForm {
    private String login;
    private String password;
    private boolean remember;

    public void parseRequest(HttpServletRequest request) throws IOException, ServletException {
        setPassword(request.getParameter(PASSWORD_FIELD));
        setLogin(request.getParameter(LOGIN_FIELD));
        setRemember(request.getParameter(REMEMBER_ME) != null);
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginForm loginForm = (LoginForm) o;
        return remember == loginForm.remember &&
                Objects.equals(login, loginForm.login) &&
                Objects.equals(password, loginForm.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, remember);
    }
}
