package initializer.execute;

import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/*
잔디체커에서 쓰이는 모든 프롭을 불러온다.
다른 테스트에 쓸 수 있는지 보기 위한 용도로 만듦
 */
@Slf4j
public class D221230_C03_LoadAllProps {

    @Test
    public void main() throws Exception {
        Initializer.ready(); // UI든 뭐든 다 불러오는 전체초기화를 수행. 프롭만 테스트하자고 구조를 뜯을 순 없어서..
        // 결론: Initializer.ready()만 하면 Initializer 필드들 잘 로딩됨.
    }

}
