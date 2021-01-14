package com.epam.preprod.pavlov.util;

import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.util.model.NotificationContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class NotificationUtil {
    private static final Logger LOGGER = Logger.getLogger(NotificationUtil.class.getName());

    private NotificationUtil() {

    }

    /**
     * Removes notification context at the invocation moment
     *
     * @param request
     */
    public static void cleanNotificationContext(HttpServletRequest request) {
        getSessionFromRequest(request).setAttribute(RequestConstants.CLEAN_ERROR_CONTEXT_FLAG, true);
    }

    /**
     * Removes notification context at the invocation moment (immediately)
     *
     * @param request
     */
    public static void cleanNotificationContextImmediately(HttpServletRequest request) {
        Map<String, NotificationContainer> notificationContext = (Map<String, NotificationContainer>) getSessionFromRequest(request).getAttribute(SessionConstants.NOTIFICATION_CONTEXT);
        if (Objects.isNull(notificationContext)) {
            throw new UnsupportedOperationException();
        }
        notificationContext.clear();
    }

    public static void addNewMessage(String notificationContainerName, String contentKey, String content, HttpServletRequest request) {
        NotificationContainer container = getSpecifiedContainer(notificationContainerName, request);
        container.getNotificationContainer().put(contentKey, content);
        addContainerToContext(request, container);
    }

    public static boolean isNotificationContainerEmpty(String notificationContainerName, HttpServletRequest request) {
        NotificationContainer container = getSpecifiedContainer(notificationContainerName, request);
        return container.getNotificationContainer().isEmpty();
    }

    public static boolean isAllNotificationContainersEmpty(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, NotificationContainer> notificationContext = (Map<String, NotificationContainer>) session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT);
        return notificationContext.entrySet().stream().allMatch((element) -> {
            NotificationContainer container = element.getValue();
            return container.getNotificationContainer().isEmpty();
        });
    }

    private static HttpSession getSessionFromRequest(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (Objects.isNull(session)) {
            throw new IllegalStateException("The session is not exist!");
        }
        return session;
    }

    private static NotificationContainer getSpecifiedContainer(String containerName, HttpServletRequest request) {
        HttpSession session = getSessionFromRequest(request);
        Map<String, NotificationContainer> notificationContext = (Map<String, NotificationContainer>) session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT);
        NotificationContainer container;
        if (Objects.isNull(notificationContext.get(containerName))) {
            container = new NotificationContainer(containerName);
            LOGGER.info(String.format("Specified notification container: %s does not exist. New container has been created by this name!", containerName));
        } else {
            container = notificationContext.get(containerName);
        }
        return container;
    }

    private static void addContainerToContext(HttpServletRequest request, NotificationContainer cotainer) {
        Map<String, NotificationContainer> context = getNotificationContext(request);
        if (Objects.isNull(context)) {
            throw new IllegalArgumentException();
        }
        context.put(cotainer.getName(), cotainer);
    }

    private static Map<String, NotificationContainer> getNotificationContext(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (Map<String, NotificationContainer>) session.getAttribute(SessionConstants.NOTIFICATION_CONTEXT);
    }

    public static int getNotificationContainerSize(String containerName, HttpServletRequest request) {
        NotificationContainer container = getSpecifiedContainer(containerName, request);
        return Objects.nonNull(container) ? container.getNotificationContainer().size() : 0;
    }

}
