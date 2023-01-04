import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.util.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class D230104_C02_ArrayListTest {

    @Test
    public void main() {

        List<String> l = new ArrayList<>();
        l.add("자바를");
        l.add("잘");
        l.add("하려면");
        l.add("어떤");
        l.add("걸");
        l.add("공부해야");
        l.add("하지?");

        String s1 = l.toString();
        log.debug(s1);

        String s2 = StringUtils.join(l, " ");
        log.debug(s2);

    }

}
