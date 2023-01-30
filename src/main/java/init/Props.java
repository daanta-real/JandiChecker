package init;

import lombok.Data;

import java.nio.file.Paths;
import java.util.Map;

@Data
public class Props {

    private static String path = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로
    private static String version;
    private static String build;
    private static String language;
    private static Map<String, String> translation;
    private static Map<String, Map<String, String>> members;
    private static String information;
    private static String cronSchedule;
    private static String cronTargetChannelID;
    private static String token_JDA;
    private static String token_ChatGPT;
    private static String token_GoogleCloud;

    public String lang(String optionName) {
        return translation.get(optionName);
    }

    public Map<String, String> member(String memberName) {
        return members.get(memberName);
    }

}
