package com.epam.preprod.pavlov.dao.impl;

import com.epam.preprod.pavlov.constant.ErrorMessages;
import com.epam.preprod.pavlov.constant.ErrorTitles;
import com.epam.preprod.pavlov.constant.sql.query.UserSqlQueries;
import com.epam.preprod.pavlov.dao.UserDao;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.exception.ApplicationException;
import com.epam.preprod.pavlov.handler.DefaultResultSetHandler;
import com.epam.preprod.pavlov.handler.ResultSetHandlers;
import com.epam.preprod.pavlov.util.ThreadLocalConnectionStorage;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final String GET_BY_LOGIN_SQL_EXCEPTION_MESSAGE = "SQL exception occurred during getByLogin method calling!";
    private static final String INSERT_USER_SQL_EXCEPTION_MESSAGE = "SQL exception has been happened during inserting into Users!";
    private static final String EXTRACTING_USER_BY_EMAIL_SQL_EXCEPTION = "Sql exception was occurs during extracting user by email!";
    private static final String EXTRACTING_ROLES_BY_ID_SQL_EXCEPTION = "Sql exception was occurs during extracting roles by user id!";
    private static final Logger USER_DAO_LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private final QueryRunner queryRunner;
    private final DefaultResultSetHandler userResultSetHandler;


    public UserDaoImpl(QueryRunner queryRunner, DefaultResultSetHandler userResultSetHandler) {
        this.queryRunner = queryRunner;
        this.userResultSetHandler = userResultSetHandler;
    }

    @Override
    public User getUserByLogin(String login) {
        Connection connection = null;
        try {
            connection = ThreadLocalConnectionStorage.getConnection();
            return (User) queryRunner.query(connection, UserSqlQueries.SELECT_USER_BY_LOGIN,
                    userResultSetHandler, login);
        } catch (SQLException e) {
            sqlExceptionHandler(GET_BY_LOGIN_SQL_EXCEPTION_MESSAGE, connection);
        }
        return null;
    }

    @Override
    public void createUser(User user) {
        Connection connection = ThreadLocalConnectionStorage.getConnection();
        try {
            queryRunner.update(connection, UserSqlQueries.INSERT_INTO_USERS,
                    user.getLogin(),
                    user.getName(),
                    user.getSurname(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getAvatar(),
                    user.isNotification()
            );
        } catch (SQLException ex) {
            sqlExceptionHandler(INSERT_USER_SQL_EXCEPTION_MESSAGE, connection);
        }
    }

    @Override
    public User getByEmail(String email) {
        Connection connection = ThreadLocalConnectionStorage.getConnection();
        try {
            return (User) queryRunner.query(connection, UserSqlQueries.SELECT_USER_BY_EMAIL, userResultSetHandler, email);
        } catch (SQLException e) {
            sqlExceptionHandler(EXTRACTING_USER_BY_EMAIL_SQL_EXCEPTION, connection);
        }
        return null;
    }

    @Override
    public List<String> getUserRoles(int userId) {
        Connection connection = ThreadLocalConnectionStorage.getConnection();
        List<String> roles = null;
        try {
            roles = queryRunner.query(connection, UserSqlQueries.SELECT_ROLES_BY_USER_ID, ResultSetHandlers.ROLES_RESULT_SET_HANDLER, userId);
        } catch (SQLException e) {
            sqlExceptionHandler(EXTRACTING_ROLES_BY_ID_SQL_EXCEPTION, connection);
        }
        return roles;
    }

    private void sqlExceptionHandler(String sqlErrorMessage, Connection connection) throws ApplicationException {
        try {
            connection.setClientInfo(ErrorTitles.SQL_EXCEPTION, ErrorMessages.SQL_EXCEPTION_BODY);
        } catch (SQLClientInfoException e) {
            USER_DAO_LOGGER.error(ErrorMessages.CLIENT_INFO_ERROR_MESSAGE);
            throw new ApplicationException(sqlErrorMessage);
        }
        USER_DAO_LOGGER.error(sqlErrorMessage);
        throw new ApplicationException(sqlErrorMessage);
    }
}
