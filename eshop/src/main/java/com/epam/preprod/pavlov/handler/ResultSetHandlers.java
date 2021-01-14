package com.epam.preprod.pavlov.handler;

import com.epam.preprod.pavlov.constant.RequestConstants;
import org.apache.commons.dbutils.ResultSetHandler;

import java.util.ArrayList;
import java.util.List;

public class ResultSetHandlers {
    private static final String ROLE_NAME_COLUMN = "Role_Name";

    private ResultSetHandlers() {
    }


    public static final ResultSetHandler<List<String>> ROLES_RESULT_SET_HANDLER = (resultSet -> {
        List<String> roles = null;
        while (resultSet.next()) {
            roles = new ArrayList<>();
            roles.add(resultSet.getString(ROLE_NAME_COLUMN));
        }
        return roles;
    });
}
