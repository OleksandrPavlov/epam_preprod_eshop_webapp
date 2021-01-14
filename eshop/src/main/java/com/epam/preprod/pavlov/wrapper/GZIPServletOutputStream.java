package com.epam.preprod.pavlov.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;


public class GZIPServletOutputStream extends ServletOutputStream {
    private static final String UNAVAILABLE_OPTION = "This option is unavailable in current implementation";
    private GZIPOutputStream gzipOutputStream;

    public GZIPServletOutputStream(ServletOutputStream servletOutputStream) throws IOException {
        super();
        this.gzipOutputStream = new GZIPOutputStream(servletOutputStream);
    }

    @Override
    public void close() throws IOException {
        this.gzipOutputStream.close();
    }

    @Override
    public void flush() throws IOException {
        this.gzipOutputStream.flush();
    }

    @Override
    public void write(byte bytes[]) throws IOException {
        this.gzipOutputStream.write(bytes);
    }

    @Override
    public void write(byte bytes[], int off, int len) throws IOException {
        this.gzipOutputStream.write(bytes, off, len);
    }

    @Override
    public boolean isReady() {
        throw new UnsupportedOperationException(UNAVAILABLE_OPTION);
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException(UNAVAILABLE_OPTION);
    }

    @Override
    public void write(int i) throws IOException {
        this.gzipOutputStream.write(i);
    }
}
