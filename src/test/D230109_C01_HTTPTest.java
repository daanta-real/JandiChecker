import crawler.Crawler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
public class D230109_C01_HTTPTest {

    private static final String id = "daanta-real";

    @Test
    public void main() throws Exception {
        log.debug("HTTP REQUEST가 잘 작동하는지 테스트합니다.");
        String url = "https://github.com/" + id;
        log.debug(url);
        String result = httpRequestUrl_GET(url);
        log.debug("HTML을 받아왔습니다. RESULT: {}", result);
        String result2 = Crawler.getHTMLByID(id);
        log.debug("결과: {}", result2);
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

    @Test
    public void falseID() throws Exception {
        String id = "Asdfasdawegwpwnpgwe";
        log.debug("result: {}", Crawler.getGithubMap(id));
    }

}
