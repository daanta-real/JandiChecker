package checking;

import crawler.Checker;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class D240214_C01_CronDailyCheckingTester {

    @BeforeAll
    public static void prepare() throws Exception {
        Initializer.ready(false);
    }

    // Daily commit checking logic tester
    @Test
    public void test() throws Exception {

        // Get the complete information string of checking result of the daily commit status of all group members
        String yesterdayCommitedString = Checker.getDidCommitYesterday_onlyIfAny();
        log.debug(yesterdayCommitedString);

        // Be aware of that the test should be passed only if there was no one committed to GitHub yesterday.
        Assertions.assertEquals("", yesterdayCommitedString);

    }

}
