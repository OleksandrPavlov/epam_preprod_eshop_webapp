package com.epam.preprod.pavlov.handler;

import com.epam.preprod.pavlov.entity.User;
import org.easymock.MockType;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.epam.preprod.pavlov.constant.sql.SqlUserConstants.USER_ID;
import static com.epam.preprod.pavlov.constant.sql.SqlUserConstants.USER_NOTIFY;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

public class DefaultResultSetHandlerTest {
    private ResultSet resultSet;

    @Before
    public void init() {
        resultSet = mock(MockType.NICE, ResultSet.class);
    }

    @Test
    public void shouldReturnListWhenHandleMethodCalled() throws SQLException {
        DefaultResultSetHandler defaultResultSetHandler = new DefaultResultSetHandler(User.class, true);
        expect(resultSet.next()).andReturn(true);
        expect(resultSet.getObject(USER_ID)).andReturn(1);
        expect(resultSet.getObject(USER_NOTIFY)).andReturn(true);
        replay(resultSet);
        Object object = defaultResultSetHandler.handle(resultSet);
        assertEquals(true, object instanceof List);
        verify(resultSet);
    }

    @Test
    public void shouldReturnEntityWhenHandleMethodCalled() throws SQLException {
        DefaultResultSetHandler defaultResultSetHandler = new DefaultResultSetHandler(User.class, false);
        expect(resultSet.next()).andReturn(true);
        expect(resultSet.getObject(USER_ID)).andReturn(1);
        expect(resultSet.getObject(USER_NOTIFY)).andReturn(true);
        replay(resultSet);
        Object object = defaultResultSetHandler.handle(resultSet);
        assertEquals(true, object instanceof User);
        verify(resultSet);
    }
}