package scrapping;

import crawler.Crawler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

@Slf4j
public class D230110_C01_HTMLTrimTest {

    private static final String html = """
                    <g transform="translate(728, 0)">
                        <rect width="10" height="10" x="-38" y="0" class="ContributionCalendar-day" data-date="2023-01-08" data-level="1" rx="2" ry="2">2 contributions on January 8, 2023</rect>
                        <rect width="10" height="10" x="-38" y="13" class="ContributionCalendar-day" data-date="2023-01-09" data-level="3" rx="2" ry="2">10 contributions on January 9, 2023</rect>
                        <rect width="10" height="10" x="-38" y="26" class="ContributionCalendar-day" data-date="2023-01-10" data-level="1" rx="2" ry="2">2 contributions on January 10, 2023</rect>
                    </g>
            """;

    @Test
    public void run() throws Exception {
        Map<String, Boolean> result = Crawler.makeMapFromTrimmed(html);
        log.debug("{}", result);
    }

}
