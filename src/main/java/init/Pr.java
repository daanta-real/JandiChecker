package init;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import utils.CommonUtils;

import java.nio.file.Paths;
import java.util.Map;

// The class holding and handling all the properties
@Slf4j
@Data
public class Pr {

    private String path;
    private String version;
    private String build;
    private String language;
    private String information1, information2;
    private String cronSchedule;
    private String cronTargetChannelID;
    private String token_JDA;
    private String token_ChatGPT;
    private String token_GoogleCloud;
    private Map<String, String> translation;
    private Map<String, Map<String, String>> members;

    public static final Pr pr = new Pr();
    private Pr() {
        path = Paths.get("").toAbsolutePath().toString();
    }

    // ★★★ Get the string as Main language from props
    public String l(String optionName) {
        return translation.get(optionName);
    }

    public Map<String, String> member(String memberName) {
        return members.get(memberName);
    }

    public void loadLanguageInfoes() throws Exception {
        String languageSettingsPath = "languages/" + language + ".yaml";
        log.info("Loading language files..: {}", languageSettingsPath);
        translation = CommonUtils.loadYaml(languageSettingsPath); // Language props loaded
        log.info("FINISHED LOADING LANGUAGES!\n\nLANGUAGES:\n{}\n", utils.CommonUtils.getPrettyJSON(translation));
    }

    public void loadVersionInfoes() throws Exception {
        Map<String, String> versionMap = CommonUtils.loadYaml("version.yaml");
        setVersion(versionMap.get("version"));
        setBuild(versionMap.get("build"));
    }

    // Get a member's GitHub ID from name
    public String getGitHubIDByMemberName(String memberName) throws Exception {
        if(!getMembers().containsKey(memberName)) throw new Exception();
        Map<String, String> memberMap = member(memberName);
        return memberMap.get("gitHubID");
    }

    // Get a member's nickname from the Discord ID
    public String getMemberNameByDiscordName(String discordName) throws Exception {
        for(Map.Entry<String, Map<String, String>> entry: getMembers().entrySet()) {
            Map<String, String> memberProps = entry.getValue();
            if(!memberProps.containsKey("discordName")) continue;
            String foundDiscordName = memberProps.get("discordName");
            if(!StringUtils.isEmpty(foundDiscordName) && foundDiscordName.equals(discordName)) {
                return memberProps.get("name");
            }
        }
        throw new Exception();
    }

    // Get the Map including member's name GitHub ID from Discord ID
    public Map<String, String> getMemberInfoesByDiscordName(String discordName) throws Exception {
        for(Map.Entry<String, Map<String, String>> entry: members.entrySet()) {
            Map<String, String> memberProp = entry.getValue();
            if(!memberProp.containsKey("discordName")) continue; // TODO test
            String foundDiscordName = memberProp.get("discordName");
            if(!StringUtils.isEmpty(foundDiscordName) && foundDiscordName.equals(discordName)) {
                return memberProp;
            }
        }
        throw new Exception();
    }

}
