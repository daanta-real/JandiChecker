import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class D221230_C04_InitializerTest {

    @Test
    public void sendHTTPRequest() throws Exception {

        Initializer.ready();
        log.debug("테스트 종료");

    }

}
