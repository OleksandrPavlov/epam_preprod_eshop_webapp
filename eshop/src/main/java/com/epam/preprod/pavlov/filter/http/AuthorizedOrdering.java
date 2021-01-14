package com.epam.preprod.pavlov.filter.http;

import com.epam.preprod.pavlov.constant.NotificationContextConstants;
import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.constant.order.NotificationMessageConstants;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.util.NotificationUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebFilter("/AuthorizedOrdering")
public class AuthorizedOrdering extends AbstractFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(SessionConstants.CURRENT_USER);
        if (Objects.isNull(currentUser)) {
            session.setAttribute(SessionConstants.UNFINISHED_ORDERING, true);
            NotificationUtil.addNewMessage(NotificationContextConstants.NOTIFICATION_BLOCK, RequestConstants.ORDER_NOTIFICATION, NotificationMessageConstants.AUTHORIZED_ORDERING, request);
            response.sendRedirect(request.getContextPath() + PathConstants.LOGIN_SERVLET);
            return;
        }
        filterChain.doFilter(request, response);
    }


}
