package scrapping;

import crawler.GitHubMap;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class D230113_C02_GetGitHubMapTest {

    @BeforeEach
    void init() throws Exception {

        // Load all preferences
        log.info("<<< INITIALIZING JandiChecker >>>");
        Initializer.ready(true);

    }

    @Test
    void makeMapFromTrimmedTest() throws Exception {
        String id = "daanta-real";
//        String html = Crawler.getHTMLByID(id);
//        TreeMap<String, Boolean> map = Crawler.makeMapFromTrimmed(html);
//        log.debug("result:\n{}", map);
    }

    @Test
    void getGitHubMapTest() {
        String id = "daanta-real";

        try {
            String result = GitHubMap.getGithubInfoString(id, id);
            log.debug("result: {}", result);
        } catch(Exception e) {
            log.error("내가 모르는 에러가 발생하였다. 자세히 살펴봐야 한다");
            e.printStackTrace();
        }

    }

    @Test
    void caseNoMapInProfilePage() {

        String id = "nullable";

        try {
            String result = GitHubMap.getGithubInfoString(id, id);
            log.debug("result: {}", result);
            assert "프로필 페이지에 잔디밭이 없어 조회하지 못했습니다.".equals(result);
        } catch(Exception e) {
            log.error("내가 모르는 에러가 발생하였다. 자세히 살펴봐야 한다");
            e.printStackTrace();
        }

    }

    @Test
    void case404() {

        String id = "asfdgasdgagragwr";

        try {
            String result = GitHubMap.getGithubInfoString(id, id);
            assert "없는 GitHub ID입니다.".equals(result);
        } catch(Exception e) {
            log.error("내가 모르는 에러가 발생하였다. 자세히 살펴봐야 한다");
            e.printStackTrace();
        }

    }

}
