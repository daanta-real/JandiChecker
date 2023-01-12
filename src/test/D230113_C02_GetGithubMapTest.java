import crawler.GithubMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class D230113_C02_GetGithubMapTest {

    @Test
    public void caseNoMapInProfilePage() {

        String id = "nullable";

        try {
            String result = GithubMap.getGithubInfoString(id, id);
            log.debug("result: {}", result);
            assert "프로필 페이지에 잔디밭이 없어 조회하지 못했습니다.".equals(result);
        } catch(Exception e) {
            log.error("내가 모르는 에러가 발생하였다. 자세히 살펴봐야 한다");
            e.printStackTrace();
        }

    }

    @Test
    public void case404() {

        String id = "asfdgasdgagragwr";

        try {
            String result = GithubMap.getGithubInfoString(id, id);
            assert "없는 GitHub ID입니다.".equals(result);
        } catch(Exception e) {
            log.error("내가 모르는 에러가 발생하였다. 자세히 살펴봐야 한다");
            e.printStackTrace();
        }

    }

}
