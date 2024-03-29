package utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static init.Pr.pr;

@Slf4j
public class CommonUtils {

    private CommonUtils() {}

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

    // Returns date Strings of given day str as "xxxx-xx-xx"
    @NotNull
    public static Calendar getDate(@NotNull String dateStr) {
        String y = dateStr.substring(0, 4);
        String m = String.valueOf(Integer.parseInt(dateStr.substring(4, 6)) - 1);
        String d = dateStr.substring(6, 8);
        return getCalendar(y, m, d);
    }

    // pause in console
    // https://stackoverflow.com/questions/6032118/make-the-console-wait-for-a-user-input-to-close
    public static void waitForEnter() { waitForEnter(""); }
    public static void waitForEnter(String message, Object... args) {
        Console c = System.console();
        if (c != null) {
            // printf-like arguments
            if (message != null) c.format(message, args);
            c.format("\n" + pr.l("pressTheEnterKey") + "...\n");
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

    // Unescape majors of HTML Entity character
    public static String unescapeHTMLEntity(String msg) {

        // Unescape the chars StringEscapeUtils supports
        String unescaped = StringEscapeUtils.unescapeHtml4(msg);

        // Forced unescape the chars StringEscapeUtils doesn't support
        String[] entityNames    = new String[] {"&nbsp;", "&lt;" ,"&gt;" , "&amp;", "&quot;", "&apos;"};
        String[] entityNumbers  = new String[] {"&#160;", "&#60;","&#62;", "&#38;", "&#34;" , "&#39;" };
        String[] originalChars  = new String[] {" "     , "<"    ,">"    , "&"    , "\""    , "'"     };
        for(int i = 0; i < entityNames.length; i++) {
            unescaped = unescaped
                    .replaceAll(entityNames[i], originalChars[i])
                    .replaceAll(entityNumbers[i], originalChars[i]);
        }

        return unescaped;

    }

    // Load a yaml file and return the prop
    public static HashMap<String, String> loadYaml(String filePath) throws Exception {

        ClassLoader loader = Initializer.class.getClassLoader();
        InputStream is = loader.getResourceAsStream(filePath);
        if(is != null) {
            log.debug("Found the language file.");
        }

        HashMap<?, ?> loadedProps = new ObjectMapper(new YAMLFactory()).readValue(is, HashMap.class);
        if (loadedProps.size() > 0) {
            log.debug("Read the language Map.");
        }

        return convertHashMap(loadedProps);

    }

    // HashMap<?, ?> to HashMap<String, Object>
    private static HashMap<String, String> convertHashMap(HashMap<?, ?> org) {
        HashMap<String, String> map = new HashMap<>();
        org.keySet().forEach(key -> {
            String keyStr = String.valueOf(key);
            String value = String.valueOf(org.get(key));
            map.put(keyStr, value);
        });
        return map;
    }

    public static String getPrettyJSON(Object o) {
        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        return gson.toJson(o);
    }

}
