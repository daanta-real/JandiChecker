package crawler;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import utils.CommonUtils;

import java.util.*;

import static init.Pr.pr;

// Core codes about GitHub scrapping
@Slf4j
public class Crawler {

	// Return the GitHub profile page of someone
	@NotNull
	public static String getHTMLByID(@NotNull String id) throws Exception {
		try {
			return CommonUtils.httpRequestUrl_GET("https://github.com/" + id);
		} catch(Exception e) {
			String msg = e.getMessage().equals("404")
					? pr.l("err_gitHubIDNotExists")
					: e.getMessage();
			throw new Exception(msg);
		}
	}

	// Trimmed HTML to commit score by date
	@NotNull
	public static TreeMap<String, Boolean> makeMapFromTrimmed(@NotNull String html) throws Exception {

		String[] htmlArr = html.split("\n");

		TreeMap<String, Boolean> m = new TreeMap<>();
		// log.debug("ORIGINAL WHOLE HTML:\n{}", html);
		for(String oneLine: htmlArr) {

			// Target only the lines including the td tag starting with ContributionCalendar-day,
			// which have each commit data of the day
			if(!oneLine.contains("ContributionCalendar-day")) {
				continue;
			}
			int idx_date = oneLine.indexOf("data-date");

			// v1.9(230814)~: Deprecated. The HTML structure has been changed from some day.
//			// While some lines don't have "data-date" attribute but start with <rect
//			if(idx_date < 0) {
//				continue;
//			}

			// Date extraction
			int idxDate_Start = idx_date + 11;
			String date
					= oneLine.substring(idxDate_Start    , idxDate_Start + 4)
					+ oneLine.substring(idxDate_Start + 5, idxDate_Start + 7)
					+ oneLine.substring(idxDate_Start + 8, idxDate_Start + 10);

			// Data extraction
			int idx_data = oneLine.indexOf("data-level");
			String grassScore = oneLine.substring(idx_data + 12, idx_data + 13);
			boolean hasCommitted = Integer.parseInt(grassScore) > 0;

//			log.debug("makeMapFromTrimmed > {} -> committed? {}", date, hasCommitted);
			m.put(date, hasCommitted);

		}
		if(m.isEmpty()) { // = size 0
			throw new Exception(pr.l("err_noCommitMapInGitHubProfile"));
		}

		String first = Collections.min(m.keySet());
		String last = Collections.max(m.keySet());
		log.debug(pr.l("crawler_trimResultOne"), m.size(), first, last);
		log.debug(pr.l("crawler_trimResultAll"), m);
		return m;

	}

	// Gets a GitHub ID and returns his annual commit map
	@NotNull
	public static TreeMap<String, Boolean> getGithubMap(@NotNull String id) throws Exception {

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