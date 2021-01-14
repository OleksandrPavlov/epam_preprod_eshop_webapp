package com.epam.preprod.pavlov.factory;

import com.epam.preprod.pavlov.entity.RegistrationForm;
import com.epam.preprod.pavlov.entity.User;

public class UserFactory {

    public static User createUser(RegistrationForm form) {
        User user = new User();
        user.setName(form.getName());
        user.setSurname(form.getSurname());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword());
        user.setNotification(form.isToNotify());
        user.setLogin(form.getLogin());
        user.setAvatar(form.getAvatarName());
        return user;
    }
}
