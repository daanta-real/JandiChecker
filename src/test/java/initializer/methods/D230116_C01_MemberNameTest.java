package initializer.methods;

import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static init.Pr.pr;

@Slf4j
public class D230116_C01_MemberNameTest {

    @Before
    public void prepare() throws Exception {
        Initializer.ready(false);
    }

    @Test
    public void getGitHubIDByMemberName() throws Exception {
        log.debug("MEMBERS: {}", pr.getMembers());
        log.debug("준성: {}", pr.getMembers().get("박준성"));
        log.debug(pr.getGitHubIDByMemberName("박준성"));
    }

    @Test
    public void findMemberPropsByDiscordName() {
        log.debug("MEMBERS: {}", pr.getMembers());
        String discordName = "rqqh35#2393";
        Map<String, String> result = null;
        for(Map.Entry<String, Map<String, String>> entry: pr.getMembers().entrySet()) {
            Map<String, String> memberProp = entry.getValue();
            log.debug("memberProp: {} => {}", entry.getKey(), memberProp);
            if(!memberProp.containsKey("discordName")) {
                log.debug("못찾음");
                continue;
            }
            String foundDiscordName = memberProp.get("discordName");
            log.debug("찾음. discordName: {}", foundDiscordName);
            if(!StringUtils.isEmpty(foundDiscordName) && foundDiscordName.equals(discordName)) {
                log.debug("찾았다! {}", memberProp);
                result = memberProp;
            } else {
                log.debug("못찾음");
            }
        }
        log.debug("RESULT: {}", result);
    }

    @Test
    public void getMemberNameByDiscordName() throws Exception {
        String discordName = "rqqh35#2393";
        String displayedName = pr.getMemberNameByDiscordName(discordName);
        log.debug("Found name: {}", displayedName);
    }

}
