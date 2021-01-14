package com.epam.preprod.pavlov.servlet.order;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.constant.PathConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.constant.order.NotificationMessageConstants;
import com.epam.preprod.pavlov.constant.sql.order.OrderIdentifiers;
import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.entity.order.*;
import com.epam.preprod.pavlov.exception.ValidationException;
import com.epam.preprod.pavlov.service.order.OrderService;
import com.epam.preprod.pavlov.servlet.AbstractServlet;
import com.epam.preprod.pavlov.util.NotificationUtil;
import com.epam.preprod.pavlov.util.ProductCartUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.epam.preprod.pavlov.constant.NotificationContextConstants.ORDER_VALIDATING_NOTE_CONTAINER_ERROR;
import static com.epam.preprod.pavlov.constant.ErrorTitles.ORDER_VALIDATING;
import static com.epam.preprod.pavlov.constant.NotificationContextConstants.ORDER_VALIDATING_NOTE_CONTAINER_SUCCESS;

@WebServlet("/makeOrder")
public class MakeOrderServlet extends AbstractServlet {
    private static final Logger MAKE_ORDER_LOGGER = LoggerFactory.getLogger(MakeOrderServlet.class);
    private static final String SUCCESS_ORDERING_NOTIFICATION_TITLE = "orderSuccess";
    private static final String SUCCESS_ORDERING_NOTIFICATION_MESSAGE = "Order has been successfully sent out!";

    private OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NotificationUtil.cleanNotificationContext(req);
        forwardTo(req, resp, PathConstants.ORDER_PAGE, PathConstants.PRODUCT_LIST_FRAME);
    }

    @Override
    public void init() {
        orderService = (OrderService) getServletContext().getAttribute(ContextConstants.ORDER_SERVICE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws IOException {
        Order order = null;
        try {
            order = orderAssembly(req);
        } catch (ValidationException ex) {
            MAKE_ORDER_LOGGER.debug("Validating exception occurred during ordering");
            NotificationUtil.addNewMessage(ORDER_VALIDATING_NOTE_CONTAINER_ERROR, ORDER_VALIDATING, ex.getMessage(), req);
        }
        if (NotificationUtil.isNotificationContainerEmpty(ORDER_VALIDATING_NOTE_CONTAINER_ERROR, req)) {
            orderService.makeOrder(order);
            ProductCartUtil.removeCartFromCookie(resp);
            NotificationUtil.addNewMessage(ORDER_VALIDATING_NOTE_CONTAINER_SUCCESS,SUCCESS_ORDERING_NOTIFICATION_TITLE,SUCCESS_ORDERING_NOTIFICATION_MESSAGE,req);
        } else {
            resp.sendRedirect(req.getContextPath() + PathConstants.MAKE_ORDER_SERVLET);
        }
        resp.sendRedirect(req.getContextPath() + PathConstants.PRODUCT_SERVLET);
    }

    private Order orderAssembly(HttpServletRequest request) {
        Order order = new Order();
        OrderPaymentType orderPaymentType = OrderPaymentType.parseFromRequest(request);
        HttpSession session = request.getSession();
        BriefOrderUserInfo userInfo = new BriefOrderUserInfo((User) session.getAttribute(SessionConstants.CURRENT_USER));
        OrderDetail orderDetail = new OrderDetail(OrderIdentifiers.ORDER_SENT_OUT_DETAIL, null);
        OrderStatus orderStatus = new OrderStatus(OrderIdentifiers.ORDER_SENT_OUT_STATUS, null);
        order.setOrderDetail(orderDetail);
        order.setOrderStatus(orderStatus);
        order.setPaymentType(orderPaymentType);
        order.setUserInfo(userInfo);
        Map<Product, Integer> productMap = ProductCartUtil.getProductCartFromCookie(request);
        order.setProductMap(productMap);
        return order;
    }
}
