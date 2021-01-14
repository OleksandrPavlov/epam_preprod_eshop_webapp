package com.epam.preprod.pavlov.constant.sql.query;

public class UserSqlQueries {
    private UserSqlQueries() {
    }

    public static final String SELECT_USER_BY_LOGIN = "SELECT * FROM Users WHERE Login=?;";
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM Users WHERE Email=?;";
    public static final String INSERT_INTO_USERS = "INSERT INTO Users (Login,First_Name,Last_Name,Email,Password,Avatar_ref,Notification) VALUES(?,?,?,?,?,?,?);";
    public static final String SELECT_ROLES_BY_USER_ID = "SELECT Role_Name FROM Roles as r INNER JOIN Role_Users as ru ON r.Role_ID = ru.Role_ID WHERE User_ID = ?;";
}
