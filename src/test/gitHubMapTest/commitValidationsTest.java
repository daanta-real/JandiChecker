package gitHubMapTest;

import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CommonUtils;

import java.util.Calendar;

import static crawler.Checker.getCommitListByDay;
import static init.Pr.pr;

@Slf4j
class commitValidationsTest {

    @BeforeEach
    void init() throws Exception {

        // Load all preferences
        log.info("<<< INITIALIZING JandiChecker >>>");
        Initializer.ready(true);

    }


    // 1. Check the all members' commit status.
    // 2. Returns the list who DID commit yesterday "ONLY IF ANY"
    //    Otherwise return the empty String (not null.)
    @Test
    void getDidCommitYesterday_OnlyAny() throws Exception {

        log.info("Request: get list of DID committed yesterday");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -21);
        String day = CommonUtils.sdf_thin.format(c.getTime());

        String[] list = getCommitListByDay(day, false);
        String date_notice = CommonUtils.sdf_dayweek.format(c.getTime());
        String result = "```md\n[" + pr.l("checker_getDidCommitYesterday_result") + "]: %s\n%s\n```";
        log.debug(result.formatted(list[0], date_notice, list[1]));

    }

}

