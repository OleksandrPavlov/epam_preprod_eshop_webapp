package com.epam.preprod.pavlov.jdbc;

public interface Transaction<T> {
    T transact();
}
