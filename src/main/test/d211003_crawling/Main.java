package test.d211003_crawling;

import java.util.Map;

@Slf4j
public class Main {

public static void main(String[] args) throws Exception {

	// 깃헙 맵 얻어오기
	Map<String, Boolean> jandi = Crawler.getGithubMap("daanta-real");
	jandi.forEach((key, val) -> {
		log.info("키:%s, 값:%s\n", key, val);
	});

} }