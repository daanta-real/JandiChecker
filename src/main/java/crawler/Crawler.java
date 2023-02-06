package crawler;

import lombok.extern.slf4j.Slf4j;
import utils.CommonUtils;

import java.util.*;

import static init.Initializer.props;

// Core codes about GitHub scrapping
@Slf4j
public class Crawler {

	// Return the GitHub profile page of someone
	public static String getHTMLByID(String id) throws Exception {
		try {
			return CommonUtils.httpRequestUrl_GET("https://github.com/" + id);
		} catch(Exception e) {
			String msg = e.getMessage().equals("404")
					? props.lang("err_gitHubIDNotExists")
					: e.getMessage();
			throw new Exception(msg);
		}
	}

	// Trimmed HTML to commit score by date
	public static TreeMap<String, Boolean> makeMapFromTrimmed(String html) throws Exception {

		String[] htmlArr = html.split("\n");

		TreeMap<String, Boolean> m = new TreeMap<>();
		// log.debug("ORIGINAL WHOLE HTML:\n{}", html);
		for(String oneline: htmlArr) {

			// Target only the lines including the rect tag
			if(!oneline.contains("<rect width")) continue; // All lines which have comitt data start with <rect
			int idx_date = oneline.indexOf("data-date");
			if(idx_date < 0) continue; // While some lines don't have "data-date" attribute but start with <rect

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

//			log.debug("makeMapFromTrimmed > {} -> committed? {}", date, hasCommitted);
			m.put(date, hasCommitted);

		}
		if(m.size() == 0) throw new Exception(props.lang("err_noCommitMapInGitHubProfile"));

		String first = Collections.min(m.keySet());
		String last = Collections.max(m.keySet());
		log.debug(props.lang("crawler_trimResultOne"), m.size(), first, last);
		log.debug(props.lang("crawler_trimResultAll"), m);
		return m;

	}

	// Gets a GitHub ID and returns his annual commit map
	public static TreeMap<String, Boolean> getGithubMap(String id) throws Exception {

		String html;
		try {
			html = getHTMLByID(id);
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}

		try {
			return makeMapFromTrimmed(html);
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}

	}

}