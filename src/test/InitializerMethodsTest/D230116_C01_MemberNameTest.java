package InitializerMethodsTest;

import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import init.Initializer;

@Slf4j
public class D230116_C01_MemberNameTest {

    @Before
    public void prepare() throws Exception {
        Initializer.ready(false);
    }

    @Test
    public void getGitHubIDByMemberName() throws Exception {
        log.debug("MEMBERS: {}", props.getMembers());
        log.debug("준성: {}", props.getMembers().get("박준성"));
        log.debug(props.getGitHubIDByMemberName("박준성"));
    }

    @Test
    public void findMemberPropsByDiscordTagID() {
        log.debug("MEMBERS: {}", props.getMembers());
        String discordTagID = "rqqh35#2393";
        Map<String, String> result = null;
        for(Map.Entry<String, Map<String, String>> entry: props.getMembers().entrySet()) {
            Map<String, String> memberProp = entry.getValue();
            log.debug("memberProp: {} => {}", entry.getKey(), memberProp);
            if(!memberProp.containsKey("discordTagID")) {
                log.debug("못찾음");
                continue;
            }
            String foundDiscordTagID = memberProp.get("discordTagID");
            log.debug("찾음. discordTagID: {}", foundDiscordTagID);
            if(!StringUtils.isEmpty(foundDiscordTagID) && foundDiscordTagID.equals(discordTagID)) {
                log.debug("찾았다! {}", memberProp);
                result = memberProp;
            } else {
                log.debug("못찾음");
            }
        }
        log.debug("RESULT: {}", result);
    }

    @Test
    public void getMemberNameByDiscordTagID() throws Exception {
        String discordTagID = "rqqh35#2393";
        String displayedName = props.getMemberNameByDiscordTagID(discordTagID);
        log.debug("Found name: {}", displayedName);
    }

}
