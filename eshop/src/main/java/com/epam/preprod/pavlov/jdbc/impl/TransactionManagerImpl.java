package com.epam.preprod.pavlov.jdbc.impl;

import com.epam.preprod.pavlov.constant.ErrorTitles;
import com.epam.preprod.pavlov.exception.ApplicationException;
import com.epam.preprod.pavlov.jdbc.Transaction;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.util.ThreadLocalConnectionStorage;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Objects;

public class TransactionManagerImpl implements TransactionManager {
    private static final Logger TRANSACTION_MANAGER_LOGGER = LoggerFactory.getLogger(TransactionManagerImpl.class);
    private static final String START_SAVE_POINT = "START";
    private BasicDataSource dataSource;
    private Savepoint startSavePoint;

    public TransactionManagerImpl(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void start() {
        if (ThreadLocalConnectionStorage.getConnection() != null) {
            throw new ApplicationException("Thread local already has connection");
        }
        Connection connection;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            startSavePoint = connection.setSavepoint(START_SAVE_POINT);
        } catch (SQLException ex) {
            TRANSACTION_MANAGER_LOGGER.error("Sql exception has been happened in start method of transaction manager");
            throw new ApplicationException("Sql exception has been happened during start of transaction: " + ex.getMessage());
        }
        ThreadLocalConnectionStorage.setConnection(connection);
    }

    public void end() {
        Connection connection = ThreadLocalConnectionStorage.getConnection();
        if (Objects.isNull(connection)) {
            TRANSACTION_MANAGER_LOGGER.error("Application exception was thrown due to attempt of appeal to empty thread local !");
            throw new ApplicationException("ThreadLocal is empty");
        }
        try {
            String exception = connection.getClientInfo(ErrorTitles.SQL_EXCEPTION);
            if (!StringUtils.isBlank(exception)) {
                connection.rollback(startSavePoint);
            } else {
                connection.commit();
            }
            connection.close();
            ThreadLocalConnectionStorage.removeConnection();
        } catch (SQLException ex) {
            TRANSACTION_MANAGER_LOGGER.error("Exception has been happened during end of transaction!");
            throw new ApplicationException("Exception has been happened during end of transaction! Transaction canceled!" + ex.getMessage());
        }
    }

    @Override
    public <T> T executeInTransaction(Transaction<T> transaction) {
        T result;
        start();
        try {
            result = transaction.transact();
        } finally {
            end();
        }
        return result;
    }

}