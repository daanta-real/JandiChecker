package PropsLoadingTest.yamlTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Slf4j
public class D230124_C01_Jackson {

    // Succeed!
    // You must use indefinite generic type(='?') to pass the type check
    // https://catsbi.oopy.io/83635cfe-1cab-43f2-a943-56a9efd83fb2
    @Test
    public void b4() throws IOException {

        InputStream is = Initializer.class.getClassLoader().getResourceAsStream("languages/Korean.yaml");
        HashMap<?, ?> props = new ObjectMapper(new YAMLFactory()).readValue(is, HashMap.class);
        log.debug("{}", props);
        log.debug("{}", props.get("appInfo"));

    }

}
