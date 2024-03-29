package scrapping;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public class D230109_C03_ParseDateStrToNumber {

    @Test
    public void test1() {

        String[] arr = new String[]{"2022-06-08", "2022-06-09", "2022-12-13", "2022-12-12", "2022-12-11", "2022-12-10"};
        String[] newArr = Arrays.stream(arr)
                .map(s -> s.replace("-", ""))
                .toArray(String[]::new);
//        Arrays.sort(arr, (x, y) -> {
//            y > x
//        });
        log.debug("newArr: {}", Arrays.toString(newArr));
        String dat = newArr[0];
        log.debug("{}", Integer.parseInt(dat));

    }

    @Test
    public void test2() {
        String date = "2022-06-08";
        int start = 0;
        String dateStr
                = date.substring(start, start + 4)
                + date.substring(start + 5, start + 7)
                + date.substring(start + 8, start + 10);
        log.debug(dateStr);
    }

    @Test
    public void test3() {
        String s1 = "2022-06-08";
        String s2 = "2022-06-09";
        log.debug("{}, {}", s2, s1);
    }

}
