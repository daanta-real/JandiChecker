package main.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import $.$;

public class Crawler {

	// ID 주면 전체 HTML 리턴해줌
	static String getHTML(String id) throws Exception {
		// 필요변수 정의
		URL url = new URL("https://github.com/" + id);
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

	    // 회신값 수신하여 문자열로 저장
	    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    for (int c; (c = in.read()) >= 0;) sb.append((char)c);

	    // 결과리턴
	    return sb.toString();
	}

	// 수신된 웹 데이터 1차 trim (불필요 태그들 제거)
	static String trim(String str) {
		int st = str.indexOf("js-calendar-graph-svg") + 26;
		int ed = str.indexOf("<text ");
		str = str.substring(st, ed);
		return str.replaceAll("^(<rect).*(data-)$", "")
		;
	}

	// 수신된 웹 데이터 2차 trim (내부 태그 정돈)
	static String makeDataCSV(String str) {

		return str.toString()

		// 1차 내부 트림
		.replaceAll("^.*(<g.*>).*\n|.*</?g.*\n|(.*data-count=\"\\d\"\\s)|(></).*(rect>)|(\\s.*<rect).*count=\"\\d\\d\"\\s", "") // 무관문자열 all삭제
		.replaceAll("\"d", "\"\nd")             // 엔터키 안쳐진거 엔터치기

		// 데이터 좌우 트림
		.replaceAll("data-date=\"", "")    // 왼쪽부분
		.replaceAll("\" data-level=", ",") // 오른쪽부분

		// 잔디심은 결과에 따른 트림
		.replaceAll("\"[1-9]\"\n", "1\n")  // true의 경우
		.replaceAll("\"0\"\n", "0\n")      // false의 경우

		.replaceAll("\n\s.*", "")
		;

	}

	// 완성된 CSV를 배열로 변환 후 리턴
	static Map<String, Boolean> CSVtoHashMap(String csv) {

		String list[] = csv.split("\n");
		Map<String, Boolean> map = new TreeMap<>();
		for(int i = 0; i < list.length; i++) {
			String[] keyValStr = list[i].split(",");
			$.pn(Arrays.toString(keyValStr));
			// 날짜 찾기
			String dateStr = keyValStr[0];
			// 잔디여부 찾기
			Boolean val = !keyValStr[1].equals("0");
			map.put(dateStr, val);
		}
		return map;
	}

	// ID를 넘기면 일일 잔디현황을 Map으로 리턴
	public static Map<String, Boolean> getGithubMap(String githubId) throws Exception {
		String str               = getHTML     (githubId);
		String trimmed           = trim        (str)     ;
		String csv               = makeDataCSV (trimmed) ;
		Map<String, Boolean> map = CSVtoHashMap(csv);
		return map;
	}
}