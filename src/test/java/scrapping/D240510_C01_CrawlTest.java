package scrapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import crawler.Crawler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

@Slf4j
class D240510_C01_CrawlTest {

    @Test
    void showGitHubMap() {
        TreeMap<String, Boolean> map = null;
        try {
            map = Crawler.getGitHubMap("daanta-real");
            String result = new ObjectMapper()
                    .configure(SerializationFeature.INDENT_OUTPUT, true)
                    .writeValueAsString(map);
            log.debug("\n맵: {}", result);
        } catch(Exception e) {
            log.error("\n에러 발생:\n{}", String.valueOf(e));
        }
    }

}
