package com.ghost.source;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionUtils {

    public static final int BUFFER_SIZE = 8 * 1024;

    /**
     * Detects is current url is valid (exist and reachable)
     * @param pathToSource
     */
    public static boolean isAllowed(String pathToSource) throws IOException {

        URL url = new URL(pathToSource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();
        return (responseCode == 200);
    }

    /**
     * Loads the content of the specified source
     * @param pathToSource as URL
     * @return contents of the source
     * @throws IOException
     */
    public static String load(String pathToSource) throws IOException {

        URL url = new URL(pathToSource);
        try(InputStream is = new BufferedInputStream(url.openConnection().getInputStream(), BUFFER_SIZE);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int length;
            while((length = is.read()) >= 0) {
                byteArrayOutputStream.write(length);
            }
            return byteArrayOutputStream.toString();
        }
    }

    /**
     * Loads the text content of the specified source
     * @param pathToSource as URL
     * @return text contents of the source
     * @throws IOException
     */
    public static String loadText(String pathToSource) throws IOException {

        StringBuilder content = new StringBuilder();
        URL url = new URL(pathToSource);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String row;
            while ((row = reader.readLine()) != null) {
                content.append(row + "\n");
            }
        }
        return content.toString();
    }
}
