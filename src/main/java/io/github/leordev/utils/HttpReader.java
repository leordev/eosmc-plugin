package io.github.leordev.utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpReader {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String getUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String body = readAll(rd);
            return body;
        } finally {
            is.close();
        }
    }

}
