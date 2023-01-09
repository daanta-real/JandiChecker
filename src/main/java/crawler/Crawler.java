package crawler;

import lombok.extern.slf4j.Slf4j;
import utils.CommonUtils;

import java.util.*;

// 깃헙 긁어오기 관련 모든 소스코드. 크롤링 및 데이터 획득
@Slf4j
public class Crawler {

	// Return the GitHub profile page of someone
	public static String getHTMLByID(String id) throws Exception {
		return CommonUtils.httpRequestUrl_GET("https://github.com/" + id);
	}

	// 수신된 웹 데이터 1차 trim (불필요 태그들 제거)
	static String trim(String str) {
		int st = str.indexOf("js-calendar-graph-svg") + 26;
		int ed = str.indexOf("<text ");
		str = str.substring(st, ed);
		return str.replaceAll("^(<rect).*(data-)$", "")
		;
	}

	// Trimmed HTML to commit score by date
	static Map<String, Boolean> makeMapFromTrimmed(String trimmedHTML) {

		String[] htmlArr = trimmedHTML.split("\n");

		TreeMap<String, Boolean> m = new TreeMap<>();
		for(String oneline: htmlArr) {

			// Target only the lines including the rect tag
			if(!oneline.contains("<rect")) continue;

			// Date extraction
			int idx_date = oneline.indexOf("data-date");
			int idx_date_start = idx_date + 11;
			String date
					= oneline.substring(idx_date_start, idx_date_start + 4)
					+ oneline.substring(idx_date_start + 5, idx_date_start + 7)
					+ oneline.substring(idx_date_start + 8, idx_date_start + 10);

			// Data extraction
			int idx_data = oneline.indexOf("data-level");
			String data = oneline.substring(idx_data + 12, idx_data + 13);
			boolean hasCommitted = Integer.parseInt(data) > 0;

//			log.debug("{}: {}점", date, data_num);
			m.put(date, hasCommitted);

		}

		return m;

	}

	// ID를 넘기면 일일 잔디현황을 Map으로 리턴
	public static Map<String, Boolean> getGithubMap(String githubId) throws Exception {
		String str               = getHTMLByID(githubId)      ;
		String trimmed           = trim        (str)          ;
		return makeMapFromTrimmed(trimmed);
	}
}