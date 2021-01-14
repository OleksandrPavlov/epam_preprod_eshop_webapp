package com.epam.preprod.pavlov.servlet;

import com.epam.preprod.pavlov.constant.SessionConstants;
import com.epam.preprod.pavlov.entity.User;
import com.epam.preprod.pavlov.service.additional.AvatarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AvatarServletTestMockito {
    private static final String AVATAR_NAME = "ava";
    private static final String CONTENT_TYPE = "image/jpeg";
    @Mock
    private ServletOutputStream servletOutputStream;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse servletResponse;
    @Mock
    private AvatarService avatarService;
    @InjectMocks
    private AvatarServlet avatarServlet;

    @Test
    public void shouldExtractAndWriteByteRepresentationOfImageIntoResponseOutputStreamWhenDoPostCalled() throws IOException, ServletException {
        User user = new User();
        user.setAvatar(AVATAR_NAME);
        byte[] bytes = new byte[]{1, 1, 1};
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SessionConstants.CURRENT_USER)).thenReturn(user);
        when(avatarService.getImageAsByte(AVATAR_NAME)).thenReturn(bytes);
        doNothing().when(servletResponse).setContentType(CONTENT_TYPE);
        doNothing().when(servletResponse).setContentLength(bytes.length);
        when(servletResponse.getOutputStream()).thenReturn(servletOutputStream);
        doNothing().when(servletOutputStream).write(bytes);
        avatarServlet.doGet(request, servletResponse);
        verify(servletOutputStream, times(1)).write(bytes);
    }
}