package gitHubScrappingTest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

@Slf4j
public class D230109_C04_HTMLEntityEscapeTest {

    @Test
    public void test1() {
        String a = "?&quot; &quot;미안하지만 아니야.&quot;";
        log.debug(a); // ?&quot; &quot;미안하지만 아니야.&quot;
        log.debug(StringEscapeUtils.escapeHtml4(a)); // ?&amp;quot; &amp;quot;미안하지만 아니야.&amp;quot;
        log.debug(StringEscapeUtils.unescapeHtml4(a)); // ?" "미안하지만 아니야."
    }

}
