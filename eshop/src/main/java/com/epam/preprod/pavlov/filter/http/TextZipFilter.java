package com.epam.preprod.pavlov.filter.http;

import com.epam.preprod.pavlov.wrapper.CustomHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class TextZipFilter extends AbstractFilter {
    private static final Logger LOGGER = Logger.getLogger(TextZipFilter.class.getName());
    private static final String ACCEPT_ENCODING_HEADER = "accept-encoding";
    private static final String CONTENT_ENCODING = "content-encoding";
    private static final String GZIP_ENCODING = "gzip";

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("TextZipFilter executing...");
        if (request.getHeader(ACCEPT_ENCODING_HEADER).contains(GZIP_ENCODING)) {
            CustomHttpServletResponse customResponse = new CustomHttpServletResponse(response);
            response.addHeader(CONTENT_ENCODING, GZIP_ENCODING);
            filterChain.doFilter(request, customResponse);
            customResponse.flushBuffer();
            customResponse.close();
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
