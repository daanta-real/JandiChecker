package scrapping;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
public class D230109_C01_HTTPTest {

    private static final String id = "daanta-real";

    @Test
    public void test() throws Exception {
        log.debug("HTTP REQUEST가 잘 작동하는지 테스트합니다.");
        String url = "https://github.com/" + id;
        log.debug(url);
        String result = httpRequestUrl_GET(url);
        log.debug("HTML을 받아왔습니다. RESULT: {}", result);
//        String result2 = Crawler.getHTMLByID(id);
//        log.debug("결과: {}", result2);
    }

    // Send GET request just only by url
    public static String httpRequestUrl_GET(String url) throws Exception {
        URL finalUrl = new URI(url).toURL();
        HttpURLConnection conn = (HttpURLConnection)finalUrl.openConnection();
        return httpRequest(conn);
    }

    // Send HTTP Request and return the result
    public static String httpRequest(HttpURLConnection conn) throws Exception {

        // Save response values as StringBuilder
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0; ) {
            sb.append((char)c);
        }

        // return result
        return sb.toString();

    }

    @Test
    public void wrongID() throws Exception {
        String url = "https://github.com/Asdfasdawegwpwnpgwe";
        URL finalUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)finalUrl.openConnection();
        log.debug("결과: {}", conn.getResponseCode());
    }

}
