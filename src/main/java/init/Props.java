package init;

import lombok.Data;

import java.nio.file.Paths;
import java.util.Map;

@Data
public class Props {

    public static String path = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로
    public static String version;
    public static String build;
    public static String lang;
    public static String langLong;
    public static Map<String, Map<String, String>> members;
    public static String info;
    public static String cronSchedule;
    public static String cronTargetChannelID;
    public static String token_JDA;
    public static String token_ChatGPT;
    public static String token_GoogleCloud;

}
