package javaTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class D221230_C01_MyFirstJUnitTest {

    @Test
    public void dummy() {
        System.out.println("첫 번째 테스트 메소드 작성");
    }

    @Test
    public void second() {
        log.debug("로깅 기능 적용 시도 성공");
    }

}
