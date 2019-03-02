package io.github.leordev.eosmc.utils;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpHandler {

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
        return readResponse(is);
    }

    public static String postUrl(String url, String body) throws IOException {
        URLConnection con = new URL(url).openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        byte[] out = body.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }

        InputStream is = http.getInputStream();
        return readResponse(is);
    }

    private static String readResponse(InputStream is) throws IOException {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String resBody = readAll(rd);
            return resBody;
        } finally {
            is.close();
        }
    }

}
