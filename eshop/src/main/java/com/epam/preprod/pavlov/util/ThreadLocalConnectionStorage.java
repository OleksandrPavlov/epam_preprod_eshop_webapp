package com.epam.preprod.pavlov.util;

import java.sql.Connection;

public final class ThreadLocalConnectionStorage {
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    private ThreadLocalConnectionStorage() {
    }

    public static Connection getConnection() {
        return threadLocal.get();
    }

    public static void setConnection(Connection connection) {
        threadLocal.set(connection);
    }

    public static void removeConnection() {
        threadLocal.remove();
    }
}
