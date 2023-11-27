package scrapping;

import crawler.Crawler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

// TARGET: Get the whole HTML and convert into GitHub Map which I can use
@Slf4j
public class D231128_C01_JSoupTest {

    private final String id = "daanta-real";
    private String html;

    // Directy written code
    @Test
    public void test() throws Exception {

        TreeMap<String, Boolean> m = Crawler.getGitHubMap("daanta-real");

        // Completed map
        String json = new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(m);
        log.debug("최종 맵 획득 완료!\n{}", json);

        TreeMap<String, Boolean> m2 = Crawler.getGitHubMap("daanta-realasdfasdf");

        // Completed map
        String json2 = new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(m2);
        log.debug("최종 맵 획득 완료!\n{}", json2);

    }

    // Implemented code into real production

}
