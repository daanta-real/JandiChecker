import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.TreeMap;

@Slf4j
public class D230110_C02_MapDefaultValue {

    @Test
    public void run() {
        TreeMap<String, Object> m = new TreeMap<>();
        log.debug("TREEMAP'S DEFAULT VALUE: {}", m);
        //log.debug("TREEMAP IS NULL?: {}", m == null); // The default value is not null but a TreeMap instance
        log.debug("TREEMAP's size is 0?: {}", m.size() == 0); // Yes, just the size is 0
    }
}
