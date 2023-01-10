package crawler;

import lombok.extern.slf4j.Slf4j;
import utils.CommonUtils;

import java.util.*;

// 깃헙 긁어오기 관련 모든 소스코드. 크롤링 및 데이터 획득
@Slf4j
public class Crawler {

	// Return the GitHub profile page of someone
	public static String getHTMLByID(String id) {
		try {
			return CommonUtils.httpRequestUrl_GET("https://github.com/" + id);
		} catch(Exception e) {
			return null;
		}
	}

	// Trimmed HTML to commit score by date
	public static TreeMap<String, Boolean> makeMapFromTrimmed(String trimmedHTML) {

		String[] htmlArr = trimmedHTML.split("\n");

		TreeMap<String, Boolean> m = new TreeMap<>();
		// log.debug("전체 HTML:\n{}", trimmedHTML);
		for(String oneline: htmlArr) {

			// Target only the lines including the rect tag
			if(!oneline.contains("<rect width")) continue; // 잔디 있는 줄은 모두 <rect로 시작
			int idx_date = oneline.indexOf("data-date");
			if(idx_date < 0) continue; // data-date가 없는데 <rect로 시작하는 애들이 있다.

			// Date extraction
			int idx_date_start = idx_date + 11;
			String date
					= oneline.substring(idx_date_start, idx_date_start + 4)
					+ oneline.substring(idx_date_start + 5, idx_date_start + 7)
					+ oneline.substring(idx_date_start + 8, idx_date_start + 10);

			// Data extraction
			int idx_data = oneline.indexOf("data-level");
			String data = oneline.substring(idx_data + 12, idx_data + 13);
			boolean hasCommitted = Integer.parseInt(data) > 0;

//			log.debug("makeMapFromTrimmed > {} 날의 잔디 여부 {}", date, hasCommitted);
			m.put(date, hasCommitted);

		}

		String first = Collections.min(m.keySet());
		String last = Collections.max(m.keySet());
		log.debug("트림 결과: 총 {}개 ({} ~ {})", m.size(), first, last);
//		log.debug("모든 트림 결과: {}", m);
		return m;

	}

	// ID를 넘기면 연간 잔디정보를 Map으로 리턴
	public static TreeMap<String, Boolean> getGithubMap(String id) throws Exception {
		TreeMap<String, Boolean> gitHubMap;
		String html = getHTMLByID(id);
		if(html == null) throw new Exception();
		gitHubMap = makeMapFromTrimmed(html);
		return gitHubMap;
	}

}