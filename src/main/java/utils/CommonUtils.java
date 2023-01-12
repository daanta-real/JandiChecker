package utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class CommonUtils {

    // Date String formatters
    public static final SimpleDateFormat sdf_thin = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat sdf_dayweek = new SimpleDateFormat("yyyy-MM-dd (EEEE)");

    // Date String validation
    public static boolean isValidDate(String input) {
        try {
            sdf_thin.setLenient(false);
            sdf_thin.parse(input);
        } catch (Exception e) { return false; }
        return true;
    }

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
        int responseCode = conn.getResponseCode();
        if(responseCode == 404) throw new Exception("404");
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

    // Unescape all of HTML Entity characters
    public static String unescapeHTMLEntity(String msg) {

        // Unescape the chars StringEscapeUtils supports
        String unescaped = StringEscapeUtils.unescapeHtml4(msg);

        // Forced unescape the chars StringEscapeUtils doesn't support
        return unescaped.replaceAll("&#39;", "'")
                .replaceAll("&quot;", "\"");

    }

}
