import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class C230110_C02_CalendarTest {

    @Test
    public void test() {
        String date_today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.debug(date_today);
    }

    @Test
    public void test2() {
        String date_today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 20230110
        String str2 = "20230111";
        String str3 = "20230109";
        log.debug("비교 결과: {}", date_today.compareTo(str2)); // -1
        log.debug("비교 결과2: {}", date_today.compareTo(str3)); // 1
    }

}
