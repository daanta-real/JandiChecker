package javaTest;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static org.junit.Assert.assertNotNull;

public class D221230_C02_ScanInput {

    // JUnit은 일반적으로 사용자의 입력을 받지 않는 것을 전제로 테스트하기 때문에, 테스트 동작 시 read-only로 동작한다.
    // 따라서 아래 테스트에서는 br.readLine() 부분에서 홀딩되며 테스트가 끝나지 않는다.
    @Test
    public void scanInput() {

        System.out.println("입력받을거다");
        String key;

        // 읽을 준비
        try (
                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader br = new BufferedReader(isr)
        ) {

            System.out.println("입력받을거라고");

            // 입력받기. readLine = 실행완료 기준은 엔터키, delim ' ' = 두 개 이상의 변수 접수 시 끊어 읽겠다
            System.out.print("문자열 좀 입력 좀 해주세요:");
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            key = st.nextToken(); // 두 개 이상 읽을 거긴 한데 한 개만 볼 거다.

            assertNotNull(key);

        } catch (IOException e) {
            System.out.println("테스트에 실패했습니다.");
        }

    }

}
