package utils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class CommonUtils {

    // Returns a Calendar Instance which is set by inputted date info
    public static Calendar getCalendar(String y, String m, String d) {
        return getCalendar(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));
    }
    public static Calendar getCalendar(int y, int m, int d) {
        Calendar c = Calendar.getInstance();
        c.set(y, m, d);
        return c;
    }

    // pause in console
    // https://stackoverflow.com/questions/6032118/make-the-console-wait-for-a-user-input-to-close
    public static void waitForEnter() { waitForEnter(""); }
    public static void waitForEnter(String message, Object... args) {
        Console c = System.console();
        if (c != null) {
            // printf-like arguments
            if (message != null) c.format(message, args);
            c.format("\n엔터 키를 누르십시오...\n");
            c.readLine();
        }
    }

    // Send GET request just only by url
    public static String httpRequestUrl_GET(String url) throws Exception {
        URL finalUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)finalUrl.openConnection();
        return httpRequest(conn);
    }
    // Send HTTP Request and return the result
    public static String httpRequest(HttpURLConnection conn) throws Exception {

        // Save response values as StringBuilder
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) sb.append((char)c);

        // return result
        return sb.toString();

    }

}
