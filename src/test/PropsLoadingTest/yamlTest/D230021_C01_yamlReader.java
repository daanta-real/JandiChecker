package PropsLoadingTest.yamlTest;

import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/*
잔디체커에서 쓰이는 모든 프롭을 불러온다.
다른 테스트에 쓸 수 있는지 보기 위한 용도로 만듦
 */
@Slf4j
public class D230021_C01_yamlReader {

    // Fields
    //public static final String PATH = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로
    Properties props;

    // Save whole yaml file to props
    public void loadYaml() throws Exception {
        String filePath = "languages/Korean.yaml";
        ClassLoader loader = Initializer.class.getClassLoader();
        InputStream is = loader.getResourceAsStream(filePath);
        assert is != null;
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8); // Attach UTF-8 reader
        try {
            props = new Properties();
            props.load(reader);
        } catch(Exception e) {
            log.error("There was an error while loading {}", filePath);
            throw new Exception(e);
        }
    }

    // FAILED: Reading string array
    @Test
    public void main() throws Exception {
        log.debug("시작");
        loadYaml();
        log.debug("{}", props.keySet());
        log.debug("{}", props.entrySet());
        String appInfoStr = props.get("appInfo").toString();
        log.debug("{}", appInfoStr);

    }

}
