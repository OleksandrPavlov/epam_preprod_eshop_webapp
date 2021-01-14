package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.util.model.NotificationContainer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractServlet extends HttpServlet {
    public static final String INNER_PAGE_ATTRIBUTE_NAME = "currentPage";

    public void forwardTo(HttpServletRequest request, HttpServletResponse response, String innerPage, String outerPage) throws ServletException, IOException {
        request.setAttribute(INNER_PAGE_ATTRIBUTE_NAME, innerPage);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(outerPage);
        requestDispatcher.forward(request, response);
    }
}
