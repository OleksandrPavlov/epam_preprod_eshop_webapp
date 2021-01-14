package com.epam.preprod.pavlov.servlet.admin;

import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.servlet.AbstractServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin")
public class AdminHomeServlet extends AbstractServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardTo(req, resp, PathConstants.ADMIN_HOME_PAGE, PathConstants.PRODUCT_LIST_FRAME);
    }
}
