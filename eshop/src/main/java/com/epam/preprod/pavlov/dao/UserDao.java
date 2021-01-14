package com.epam.preprod.pavlov.dao;

import com.epam.preprod.pavlov.entity.User;

import java.util.List;

/**
 * @author Aleksandr Pavlov
 * @Version 1.0
 * This interface provides basic points to database interacting.
 */
public interface UserDao {
    /**
     * This method build user entity from database result set based on select query by login.
     *
     * @param login login using to find particular user row in database;
     * @return new specified User or Null in case of nothing was founded.
     */
    User getUserByLogin(final String login);

    /**
     * This method inserts new User entity to the database.
     *
     * @param user user that will be passed to database.
     */
    void createUser(User user);

    /**
     * This method build user entity from database result set based on select query by email.
     *
     * @param email login using to find particular user row in database;
     * @return new specified User or Null in case of nothing was founded.
     */
    User getByEmail(final String email);

    /**
     * This method extract user roles by user identifier from repository
     *
     * @param userId identifier
     * @return Collection of roles related to particular user
     */
    List<String> getUserRoles(int userId);

}
