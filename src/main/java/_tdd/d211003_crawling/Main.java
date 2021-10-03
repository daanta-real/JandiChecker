package _tdd.d211003_crawling;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import $.$;

public class Main {

public static void main(String[] args) throws Exception {

	// 깃헙 맵 얻어오기
	Map<Calendar, Boolean> jandi = Crawler.getGithubMap("daanta-real");
	jandi.forEach((key, val) -> {
		$.pf("키:%s, 값:%s\n", new SimpleDateFormat("yyyy/MM/dd").format(key.getTime()), val);
	});

} }