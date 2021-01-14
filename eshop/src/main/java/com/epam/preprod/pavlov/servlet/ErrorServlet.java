package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.PathConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error")
public class ErrorServlet extends AbstractServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardTo(req, resp, PathConstants.ERROR_PAGE, PathConstants.PRODUCT_LIST_FRAME);
    }
}
