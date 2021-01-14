package com.epam.preprod.pavlov.entity.order;

import com.epam.preprod.pavlov.entity.User;

public class BriefOrderUserInfo {
    private int userId;
    private String userName;
    private String userSurname;

    public BriefOrderUserInfo(User user) {
        this.userId = user.getId();
        this.userName = user.getName();
        this.userSurname = user.getSurname();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }
}
