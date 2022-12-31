import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import utils.CommonUtils;

import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class D221230_C04_GoogeTranslateAPI {

    @Test
    public void sendHTTPRequest() throws Exception {

        Initializer.ready();
        log.debug("변수 로딩 완료. 테스트 시작");

        URL url = new URL("https://translation.googleapis.com/language/translate/v2");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        String result = CommonUtils.httpRequest(conn);

        log.debug("테스트 종료");

    }

}
