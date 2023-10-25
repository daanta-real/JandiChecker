package scrapping;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class D230109_C02_MakeCSVTest {

//    private static String trimmed;
//    private static String PATH = Paths.get("").toAbsolutePath().toString();
//
//    @BeforeAll
//    public static void prepare() throws IOException {
//        trimmed = FileUtils.readFileToString(new File(PATH, "/src/test/resources/csvMakerTest.txt"), "UTF-8");
//    }
//
    private static final String target = """
        <g transform="translate(15, 20)" data-hydro-click="{&quot;event_type&quot;:&quot;user_profile.click&quot;,&quot;payload&quot;:{&quot;profile_user_id&quot;:80992630,&quot;target&quot;:&quot;CONTRIBUTION_CALENDAR_SQUARE&quot;,&quot;user_id&quot;:null,&quot;originating_url&quot;:&quot;https://github.com/sdsss&quot;}}" data-hydro-click-hmac="d86f1f18dd2ec339a1fc17cf686016e3f041fd44960bfe7df57799c826d7b96d">

              <g transform="translate(0, 0)">
                  <rect width="11" height="11" x="16" y="0" class="ContributionCalendar-day" data-date="2022-01-09" data-level="1" rx="2" ry="2">6 contributions on January 9, 2022</rect>
                  <rect width="11" height="11" x="16" y="90" class="ContributionCalendar-day" data-date="2022-01-15" data-level="3" rx="2" ry="2">17 contributions on January 15, 2022</rect>
              </g>""";
//
//    private static String[] org2 = new String[] {
//            "<g transform=\"translate(0, 0)\">\n",
//            "                      <rect width=\"11\" height=\"11\" x=\"16\" y=\"0\" class=\"ContributionCalendar-day\" data-date=\"2022-01-09\" data-level=\"1\" rx=\"2\" ry=\"2\">6 contributions on January 9, 2022</rect>\n",
//            "                      <rect width=\"11\" height=\"11\" x=\"16\" y=\"90\" class=\"ContributionCalendar-day\" data-date=\"2022-01-15\" data-level=\"3\" rx=\"2\" ry=\"2\">17 contributions on January 15, 2022</rect>\n",
//            "                  </g>"
//    };
//    private static String tx = org2[1];

    @Test
    public void main() {


        //log.debug(makeDataCSV(org));
        // 옛날엔 CSV 파싱한다고 Regex를 여러 바퀴 돌려가면서 하나하나 다 땄는데 생각해 보니 그럴 필요가 전혀 없다.
        // 그냥 Substring으로 따도 되겠다.

        // 날짜를 얻어낸다
        String[] htmlArr = target.split("\n");

        Map<String, Integer> m = new HashMap<>();
        for(String oneline: htmlArr) {

            // Target only the lines including the rect tag
            if(!oneline.contains("<rect")) continue;

            // Date extraction
            int idx_date = oneline.indexOf("data-date");
            String date = oneline.substring(idx_date + 11, idx_date + 21);

            // Data extraction
            int idx_data = oneline.indexOf("data-level");
            String data = oneline.substring(idx_data + 12, idx_data + 13);
            int data_num = Integer.parseInt(data) > 0 ? 1 : 0;

            log.debug("{}: {}점", date, data_num);
            m.put(date, data_num);

        }

        log.debug("최종 MAP: {}", m);

    }
}
