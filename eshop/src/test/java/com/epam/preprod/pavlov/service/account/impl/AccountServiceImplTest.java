package com.epam.preprod.pavlov.service.account.impl;

import com.epam.preprod.pavlov.dao.UserDao;
import com.epam.preprod.pavlov.entity.LoginForm;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.exception.AuthorizationException;
import com.epam.preprod.pavlov.exception.UserException;
import com.epam.preprod.pavlov.jdbc.Transaction;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.service.account.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    private static final User USER_1 = new User();
    private static final String LOGIN_1 = "Login";
    private static final String EMAIL_1 = "Email";
    private static final String PASSWORD_1 = "Password";
    private static final String WRONG_PASSWORD_1 = "Password1";
    @Mock
    private UserDao userDao;
    @Mock
    private TransactionManager transactionManager;
    @Mock
    private Connection connection;
    @Captor
    private ArgumentCaptor<Transaction<User>> userCaptor;

    private AccountService accountService;


    @Before
    public void init() {
        accountService = new AccountServiceImpl(userDao, transactionManager);
        USER_1.setPassword(WRONG_PASSWORD_1);
    }


    @Test
    public void shouldCreateNewUserWhenCreateMethodCalled() throws SQLException, UserException {
        when(transactionManager.executeInTransaction(any(Transaction.class))).thenReturn(true);
        accountService.create(USER_1);
        Mockito.verify(transactionManager).executeInTransaction(any(Transaction.class));
    }


    @Test
    public void shouldReturnUserWhenGetByLoginMethodCalled() throws SQLException {
        accountService.getByLogin(LOGIN_1);
        verify(transactionManager).executeInTransaction(userCaptor.capture());
        Transaction<User> transaction = userCaptor.getValue();
        transaction.transact();
        verify(userDao).getUserByLogin(LOGIN_1);
    }


    @Test
    public void shouldReturnUserWhenGetByEmailMethodCalled() throws SQLException {
        accountService.getByEmail(EMAIL_1);
        verify(transactionManager).executeInTransaction(userCaptor.capture());
        Transaction<User> transaction = userCaptor.getValue();
        transaction.transact();
        verify(userDao).getByEmail(EMAIL_1);
    }

    @Test
    public void shouldReturnUserWhenLoginMethodCalledWithRightCredentials() throws SQLException, AuthorizationException {

        when(transactionManager.executeInTransaction(userCaptor.capture())).thenReturn(USER_1);
        when(userDao.getUserByLogin(LOGIN_1)).thenReturn(USER_1);
        accountService.getByLogin(LOGIN_1);

        Transaction<User> transaction = userCaptor.getValue();
        transaction.transact();

        verify(transactionManager).executeInTransaction(userCaptor.getValue());
        verify(userDao).getUserByLogin(LOGIN_1);
    }

    @Test(expected = AuthorizationException.class)
    public void shouldThrowAnLoginExceptionWhenLoginMethodCalledWithWrongCredentials() throws SQLException, AuthorizationException {
        when(transactionManager.executeInTransaction(userCaptor.capture())).thenReturn(USER_1);
        LoginForm loginForm = new LoginForm();
        loginForm.setLogin(LOGIN_1);
        loginForm.setPassword(PASSWORD_1);
        accountService.login(loginForm);
    }


}