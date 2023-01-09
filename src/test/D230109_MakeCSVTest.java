import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
public class D230109_MakeCSVTest {

//    private static String trimmed;
//    private static String PATH = Paths.get("").toAbsolutePath().toString();
//
//    @BeforeAll
//    public static void prepare() throws IOException {
//        trimmed = FileUtils.readFileToString(new File(PATH, "/src/test/resources/csvMakerTest.txt"), "UTF-8");
//    }

    static String makeDataCSV(String str) {

        return str

                // 1차 내부 트림
                .replaceAll("^.*(<g.*>).*\n|.*</?g.*\n|(.*data-count=\"\\d\"\\s)|(></).*(rect>)|(\\s.*<rect).*count=\"\\d\\d\"\\s", "") // 무관문자열 all삭제
                .replaceAll("\"d", "\"\nd")             // 엔터키 안쳐진거 엔터치기

                // 데이터 좌우 트림
                .replaceAll("data-date=\"", "")    // 왼쪽부분
                .replaceAll("\" data-level=", ",") // 오른쪽부분

                // 잔디심은 결과에 따른 트림
                .replaceAll("\"[1-9]\"\n", "1\n")  // true의 경우
                .replaceAll("\"0\"\n", "0\n")      // false의 경우

                .replaceAll("\\n\\s.*", "")
                ;

    }

    @Test
    public void main() {

       String org = """
            <g transform="translate(15, 20)" data-hydro-click="{&quot;event_type&quot;:&quot;user_profile.click&quot;,&quot;payload&quot;:{&quot;profile_user_id&quot;:80992630,&quot;target&quot;:&quot;CONTRIBUTION_CALENDAR_SQUARE&quot;,&quot;user_id&quot;:null,&quot;originating_url&quot;:&quot;https://github.com/Kangsunmo3230&quot;}}" data-hydro-click-hmac="d86f1f18dd2ec339a1fc17cf686016e3f041fd44960bfe7df57799c826d7b96d">
                        
                  <g transform="translate(0, 0)">
                      <rect width="11" height="11" x="16" y="0" class="ContributionCalendar-day" data-date="2022-01-09" data-level="1" rx="2" ry="2">6 contributions on January 9, 2022</rect>
                      <rect width="11" height="11" x="16" y="90" class="ContributionCalendar-day" data-date="2022-01-15" data-level="3" rx="2" ry="2">17 contributions on January 15, 2022</rect>
                  </g>""";

        log.debug("LENGTH: {}", org.length());
        log.debug(makeDataCSV(org));
    }
}
