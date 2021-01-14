package com.epam.preprod.pavlov.dao.impl;

import com.epam.preprod.pavlov.constant.sql.query.UserSqlQueries;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.handler.DefaultResultSetHandler;
import com.epam.preprod.pavlov.util.ThreadLocalConnectionStorage;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class UserDaoImplTest {
    private static final User USER_1 = new User(1, "name", "surname", "password", "email", true, "avatar", "login");
    private static final String LOGIN= "login";
    private static final String EMAIL= "email";
    private Connection connection;
    private QueryRunner queryRunner = new QueryRunner();
    private DefaultResultSetHandler defaultResultSetHandler;

    @Before
    public void beforeMethod() {
        connection = mock(Connection.class);
        queryRunner = mock(QueryRunner.class);
        defaultResultSetHandler = mock(DefaultResultSetHandler.class);
    }

    @Test
    public void shouldReturnUserByLoginWhenGetByLoginMethodCalled() throws SQLException {
        expect(queryRunner.query(connection, UserSqlQueries.SELECT_USER_BY_LOGIN, defaultResultSetHandler, "login")).andReturn(null);
        replay(connection, queryRunner, defaultResultSetHandler);
        ThreadLocalConnectionStorage.setConnection(connection);
        UserDaoImpl userDao = new UserDaoImpl(queryRunner, defaultResultSetHandler);
        userDao.getUserByLogin(LOGIN);
        verify(connection, queryRunner, defaultResultSetHandler);
    }

    @Test
    public void shouldCreateUserWhenCreateUserCalled() throws SQLException {
        expect(queryRunner.update(connection, UserSqlQueries.INSERT_INTO_USERS,USER_1.getLogin(),USER_1.getName(),USER_1.getSurname(),
                USER_1.getEmail(),USER_1.getPassword(),USER_1.getAvatar(),true)).andReturn(1);
        replay(connection, queryRunner, defaultResultSetHandler);
        ThreadLocalConnectionStorage.setConnection(connection);
        UserDaoImpl userDao = new UserDaoImpl(queryRunner, defaultResultSetHandler);
        userDao.createUser(USER_1);
        verify(connection, queryRunner, defaultResultSetHandler);
    }

    @Test
    public void shouldReturnUserByEmailWhenGetUserByEmailCalled() throws SQLException {
        expect(queryRunner.query(connection, UserSqlQueries.SELECT_USER_BY_EMAIL, defaultResultSetHandler, EMAIL)).andReturn(null);
        replay(queryRunner, connection, defaultResultSetHandler);
        ThreadLocalConnectionStorage.setConnection(connection);
        UserDaoImpl userDao = new UserDaoImpl(queryRunner, defaultResultSetHandler);
        userDao.getByEmail(EMAIL);
        verify(queryRunner, connection, defaultResultSetHandler);
    }
}