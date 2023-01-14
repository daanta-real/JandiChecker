package javaTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class D230113_C01_ExceptionTest {

    public String makeException() throws Exception {
        throw new Exception("에러내용");
    }

    @Test
    public void test() {
        try {
            String a = makeException();
        } catch(Exception e) {
            log.debug(e.toString()); // "java.lang.Exception: 에러내용"
            log.debug(e.getMessage()); // "에러내용"
        }

    }
}
