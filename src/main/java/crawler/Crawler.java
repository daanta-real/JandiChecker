package crawler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.TreeMap;

import static init.Pr.pr;

// Core codes about GitHub scrapping
@Slf4j
public class Crawler {

	private Crawler() {}

	// Gets a GitHub ID and returns his annual commit map
	@NotNull
	public static TreeMap<String, Boolean> getGitHubMap(@NotNull String id) throws Exception {

		// Prepare variables
		String url = "https://github.com/" + id;
		TreeMap<String, Boolean> result = new TreeMap<>();

		// HTML → TreeMap
		try {
			Connection.Response response = Jsoup.connect(url).execute();
			Document doc = Jsoup.parse(response.body());
			Elements tbody = doc.select("table.ContributionCalendar-grid > tbody > tr > td");

			// Extract jandi map from HTML
			for (Element td : tbody) {
//				log.debug("TD:{}", td.toString());
				String keyStr = td.attr("data-date");
				String valStr = td.attr("data-level");
				if (StringUtils.isNumeric(valStr)) {
					String key
							= keyStr.substring(0, 4)
							+ keyStr.substring(5, 7)
							+ keyStr.substring(8, 10);
					boolean hasCommit
							= StringUtils.isNumeric(valStr)
							&& Integer.parseInt(valStr) > 0;
					result.put(key, hasCommit);
				}
			}

		} catch(HttpStatusException e) {
			if(e.getStatusCode() == 404) throw new Exception(pr.l("err_gitHubIDNotExists"));
		} catch(Exception e) {
			throw new Exception();
		}

		return result;

	}

}