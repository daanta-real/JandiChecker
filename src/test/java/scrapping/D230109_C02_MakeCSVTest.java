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
        <tr style="height: 10px">
          <td class="ContributionCalendar-label" style="position: relative">
            <span class="sr-only">Sunday</span>
            <span aria-hidden="true" style="clip-path: Circle(0); position: absolute; bottom: -3px">
              Sun
            </span>
          </td>

              <td class="ContributionCalendar-day" tabindex="0" data-ix="0" aria-selected="false" aria-describedby="contribution-graph-legend-level-1" style="width: 10px" data-date="2022-10-23" data-level="1"><span class="sr-only">1 contribution on October 23rd.</span></td>
              <td class="ContributionCalendar-day" tabindex="0" data-ix="1" aria-selected="false" aria-describedby="contribution-graph-legend-level-1" style="width: 10px" data-date="2022-10-30" data-level="1"><span class="sr-only">1 contribution on October 30th.</span></td>
              <td class="ContributionCalendar-day" tabindex="0" data-ix="2" aria-selected="false" aria-describedby="contribution-graph-legend-level-1" style="width: 10px" data-date="2022-11-06" data-level="1"><span class="sr-only">1 contribution on November 6th.</span></td>
              <td class="ContributionCalendar-day" tabindex="0" data-ix="3" aria-selected="false" aria-describedby="contribution-graph-legend-level-1" style="width: 10px" data-date="2022-11-13" data-level="1"><span class="sr-only">1 contribution on November 13th.</span></td>
""";

    @Test
    public void test() {


        //log.debug(makeDataCSV(org));
        // 옛날엔 CSV 파싱한다고 Regex를 여러 바퀴 돌려가면서 하나하나 다 땄는데 생각해 보니 그럴 필요가 전혀 없다.
        // 그냥 Substring으로 따도 되겠다.

        // 날짜를 얻어낸다
        String[] htmlArr = target.split("\n");

        Map<String, Integer> m = new HashMap<>();
        for(String oneline: htmlArr) {

            // Target only the lines including the rect tag
            if(!oneline.contains("ContributionCalendar-day")) {
                continue;
            } else {
                log.debug("▶ I found the contribution history tag!");
            }

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
