package javaTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import utils.CommonUtils;

@Slf4j
public class D230123_0427_StringReplaceTest {

    @Test
    public void main() {

        String a = """
			```md
			{}```
			""".formatted("AppInfo <1> <2>");
        String replaced = a.replaceFirst("<1>", "no1");
        String replaced2 = a.replaceFirst("<2>", "no2");
        log.debug("{}\n{}\n{}", a, replaced, replaced2);

    }

    @Test
    public void test2() throws Exception {

        String loaded = CommonUtils.loadYaml("languages/korean.yaml").get("appInfo").toString();

        String a = loaded.formatted("AppInfo %s %s")
                        .formatted("1", "2");
        log.debug("{}", loaded);
        log.debug("{}", a);

    }
}
