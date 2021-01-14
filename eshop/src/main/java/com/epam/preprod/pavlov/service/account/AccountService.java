package com.epam.preprod.pavlov.service.account;

import com.epam.preprod.pavlov.entity.LoginForm;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.exception.AuthorizationException;
import com.epam.preprod.pavlov.exception.UserException;

/**
 * This interface provides high-level points of interacting with underlying dao objects.
 * It represents service layer in MVC pattern.
 */
public interface AccountService {
    /**
     * This method creates new user entity in database.
     *
     * @param user object that will be passed
     * @throws UserException if some internal SQlException was occurred.
     */
    void create(User user) throws UserException;

    /**
     * This method returns User fonded by login-field.
     *
     * @param login string that will be crucial in search in database.
     * @return created User or null if nothing was found.
     */
    User getByLogin(final String login);

    /**
     * This method returns User fonded by login-field.
     *
     * @param email string that will be crucial in search in database.
     * @return created User or null if nothing was found.
     */
    User getByEmail(final String email);

    /**
     * This method determines uniqueness of user based on loginForm.
     *
     * @param loginForm
     * @return User if data in loginForm are unique related to database data.
     * @throws AuthorizationException if data in loginForm are not unique.
     */
    User login(LoginForm loginForm) throws AuthorizationException;

}
