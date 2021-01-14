package com.epam.preprod.pavlov.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Objects;


public class CustomHttpServletResponse extends HttpServletResponseWrapper {
    private GZIPServletOutputStream gzipOutputStream;
    private PrintWriter gzipPrintWriter;

    public CustomHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void flushBuffer() throws IOException {
        if (Objects.nonNull(gzipPrintWriter)) {
            gzipPrintWriter.flush();
        }
        if (Objects.nonNull(gzipOutputStream)) {
            gzipOutputStream.flush();
        }
        super.flushBuffer();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (Objects.nonNull(gzipOutputStream)) {
            throw new IllegalArgumentException("Print writer already obtained");
        }
        if (Objects.isNull(gzipPrintWriter)) {
            this.gzipPrintWriter = new PrintWriter(new OutputStreamWriter(new GZIPServletOutputStream(getResponse().getOutputStream()), getResponse().getCharacterEncoding()));
        }
        return this.gzipPrintWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (Objects.nonNull(this.gzipPrintWriter)) {
            throw new IllegalArgumentException("OutputStream already obtained");
        }
        if (Objects.isNull(this.gzipOutputStream)) {
            this.gzipOutputStream = new GZIPServletOutputStream(getResponse().getOutputStream());
        }
        return this.gzipOutputStream;
    }

    public void close() throws IOException {
        if (Objects.nonNull(gzipPrintWriter)) {
            gzipPrintWriter.close();
        }
        if (Objects.nonNull(gzipOutputStream)) {
            gzipOutputStream.close();
        }
    }
}
