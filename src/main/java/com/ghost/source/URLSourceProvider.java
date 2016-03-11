package com.ghost.source;

import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class URLSourceProvider {
    public static final int BUFFER_SIZE = 8 * 1024;

    public boolean isAllowed(String pathToSource) {
        try {
            new URL(pathToSource);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    public String load(String pathToSource) throws IOException {

        URL url = new URL(pathToSource);
        try(InputStream is = new BufferedInputStream(url.openConnection().getInputStream(), BUFFER_SIZE);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int length;
            while((length = is.read()) >= 0) {
                byteArrayOutputStream.write(length);
            }
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
