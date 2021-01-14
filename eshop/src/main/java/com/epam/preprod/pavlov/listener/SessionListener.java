package com.epam.preprod.pavlov.listener;

import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.util.model.NotificationContainer;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setAttribute(SessionConstants.NOTIFICATION_CONTEXT, new HashMap<String, NotificationContainer>());
        System.out.println("SESSION CONTAINER HAS BEEN INIT");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().removeAttribute(SessionConstants.NOTIFICATION_CONTEXT);
        System.out.println("SESSION CONTAINER HAS BEEN DESTR");
    }
}
