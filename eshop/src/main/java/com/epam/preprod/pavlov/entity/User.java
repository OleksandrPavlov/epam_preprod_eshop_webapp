package com.epam.preprod.pavlov.entity;


import com.epam.preprod.pavlov.annotation.Column;
import com.epam.preprod.pavlov.constant.sql.SqlUserConstants;

import java.util.List;
import java.util.Objects;

public class User {
    @Column(name = SqlUserConstants.USER_ID)
    private int id;
    @Column(name = SqlUserConstants.USER_NAME)
    private String name;
    @Column(name = SqlUserConstants.USER_SURNAME)
    private String surname;
    @Column(name = SqlUserConstants.USER_PASSWORD)
    private String password;
    @Column(name = SqlUserConstants.USER_EMAIL)
    private String email;
    @Column(name = SqlUserConstants.USER_NOTIFY)
    private boolean notification;
    @Column(name = SqlUserConstants.USER_AVATAR)
    private String avatar;
    @Column(name = SqlUserConstants.USER_LOGIN)
    private String login;
    private List<String> roles;

    public User() {
    }

    public User(int id, String name, String surname, String password, String email, boolean notification, String avatar, String login) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.notification = notification;
        this.avatar = avatar;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id &&
                notification == user.notification &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(avatar, user.avatar) &&
                Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, password, email, notification, avatar, login);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
