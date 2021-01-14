package com.epam.preprod.pavlov.service.account.impl;

import com.epam.preprod.pavlov.dao.UserDao;
import com.epam.preprod.pavlov.entity.LoginForm;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.exception.AuthorizationException;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;


public class AccountServiceImpl implements AccountService {
    public static final Logger ACCOUNT_SERVICE_LOGGER = LoggerFactory.getLogger(AccountService.class);
    private UserDao userDao;
    private TransactionManager transactionManager;

    public AccountServiceImpl(UserDao userDao, TransactionManager transactionManager) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
    }

    @Override
    public void create(User user) {
        transactionManager.executeInTransaction(() -> {
            userDao.createUser(user);
            return true;
        });

    }

    @Override
    public User getByLogin(final String login) {
        return transactionManager.executeInTransaction(() -> {
            User user = userDao.getUserByLogin(login);
            hangRoles(user);
            return user;
        });
    }

    @Override
    public User getByEmail(String email) {
        User user = transactionManager.executeInTransaction(() -> userDao.getByEmail(email));
        hangRoles(user);
        return user;
    }

    @Override
    public User login(LoginForm loginForm) throws AuthorizationException {
        User user = getByLogin(loginForm.getLogin());
        if (Objects.isNull(user) || !user.getPassword().equals(loginForm.getPassword())) {
            ACCOUNT_SERVICE_LOGGER.debug("AccountException was occurred during attempt to login");
            throw new AuthorizationException("User by this credentials does not exist!");
        }
        return user;
    }

    private void hangRoles(User user) {
        if (Objects.nonNull(user)) {
            List<String> roles = userDao.getUserRoles(user.getId());
            user.setRoles(roles);
        }
    }

}
