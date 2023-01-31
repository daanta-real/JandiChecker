package init;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import utils.CommonUtils;

import java.nio.file.Paths;
import java.util.Map;

@Data
public class Props {

    private String path;
    private String version;
    private String build;
    private String language;
    private String information;
    private String cronSchedule;
    private String cronTargetChannelID;
    private String token_JDA;
    private String token_ChatGPT;
    private String token_GoogleCloud;
    private Map<String, String> translation;
    private Map<String, Map<String, String>> members;

    public Props() {
        path = Paths.get("").toAbsolutePath().toString();
    }

    public String lang(String optionName) {
        return translation.get(optionName);
    }

    public Map<String, String> member(String memberName) {
        return members.get(memberName);
    }

    public void loadLanguageInfoes() throws Exception {
        String languageSettingsPath = "languages/" + language + ".yaml";
        translation = CommonUtils.loadYaml(languageSettingsPath); // Language props loaded
    }

    public void loadVersionInfoes() throws Exception {
        Map<String, String> versionMap = CommonUtils.loadYaml("version.yaml");
        setVersion(versionMap.get("version"));
        setBuild(versionMap.get("build"));
    }

    // 그룹원명을 입력하면 깃헙 ID를 리턴
    public String getGitHubIDByMemberName(String memberName) throws Exception {
        if(!getMembers().containsKey(memberName)) throw new Exception();
        Map<String, String> memberMap = member(memberName);
        return memberMap.get("gitHubID");
    }

    // 디스코드 ID를 입력하면 멤버명을 리턴
    public String getMemberNameByDiscordTagID(String discordTagID) throws Exception {
        for(Map.Entry<String, Map<String, String>> entry: getMembers().entrySet()) {
            Map<String, String> memberProps = entry.getValue();
            if(!memberProps.containsKey("discordTagID")) continue;
            String foundDiscordTagID = memberProps.get("discordTagID");
            if(!StringUtils.isEmpty(foundDiscordTagID) && foundDiscordTagID.equals(discordTagID)) {
                return memberProps.get("name");
            }
        }
        throw new Exception();
    }

    // 디스코드 ID로 그룹원의 이름과 GitHub ID를 Map으로 리턴
    public Map<String, String> getMemberInfoesByDiscordTagID(String discordTagID) throws Exception {
        for(Map.Entry<String, Map<String, String>> entry: members.entrySet()) {
            Map<String, String> memberProp = entry.getValue();
            if(!memberProp.containsKey("discordTagID")) continue; // TODO test
            String foundDiscordTagID = memberProp.get("discordTagID");
            if(!StringUtils.isEmpty(foundDiscordTagID) && foundDiscordTagID.equals(discordTagID)) {
                return memberProp;
            }
        }
        throw new Exception();
    }

}
