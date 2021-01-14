package com.epam.preprod.pavlov.jdbc.impl;

import com.epam.preprod.pavlov.constant.ErrorMessages;
import com.epam.preprod.pavlov.constant.ErrorTitles;
import com.epam.preprod.pavlov.jdbc.TransactionManager;
import com.epam.preprod.pavlov.util.ThreadLocalConnectionStorage;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class TransactionManagerImplTest {
    private static final Savepoint START_TRAN_POINT = new Savepoint() {
        @Override
        public int getSavepointId() throws SQLException {
            return 0;
        }

        @Override
        public String getSavepointName() throws SQLException {
            return "START";
        }
    };
    private Connection connection;
    private BasicDataSource dataSource;

    @Before
    public void init() {
        dataSource = mock(BasicDataSource.class);
        connection = mock(Connection.class);
    }


    @Test
    public void shouldInvokeProperMethodsWhenStartAndEndMethodsCalled() throws SQLException {
        ThreadLocalConnectionStorage.removeConnection();
        expect(dataSource.getConnection()).andReturn(connection);
        connection.setAutoCommit(false);
        expect(connection.setSavepoint("START")).andReturn(START_TRAN_POINT);
        expect(connection.getClientInfo(ErrorTitles.SQL_EXCEPTION)).andReturn(ErrorMessages.SQL_EXCEPTION_BODY);
        connection.rollback(START_TRAN_POINT);
        connection.close();
        replay(connection, dataSource);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        transactionManager.start();
        transactionManager.end();
        verify(connection, dataSource);
    }
}