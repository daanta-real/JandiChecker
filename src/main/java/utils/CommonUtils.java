package utils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        // User-Agent 헤더를 꼭 넣어줘야 한다. 넣어주지 않으면 '오늘자'의 커밋이 입수되지 않는다.
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
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
