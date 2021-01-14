package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.service.additional.AvatarService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/home/avatar")
public class AvatarServlet extends AbstractServlet {
    private static Logger log = Logger.getLogger(AvatarServlet.class.getName());
    private static final String IMAGE_CONTENT_TYPE = "image/jpeg";
    private AvatarService avatarService;

    @Override
    public void init() throws ServletException {
        log.info("Initializing AvatarServlet");
        avatarService = (AvatarService) getServletContext().getAttribute(ContextConstants.AVATAR_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(SessionConstants.CURRENT_USER);
        byte[] imageBytes = avatarService.getImageAsByte(currentUser.getAvatar());
        resp.setContentType(IMAGE_CONTENT_TYPE);
        resp.setContentLength(imageBytes.length);
        resp.getOutputStream().write(imageBytes);
        resp.flushBuffer();
        resp.getOutputStream().close();
    }

}
