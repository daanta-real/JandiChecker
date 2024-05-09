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

import java.io.IOException;
import java.util.TreeMap;

import static init.Pr.pr;

// Core codes about GitHub scrapping
@Slf4j
public class Crawler {

	private Crawler() {}

	// Gets a GitHub ID and returns his annual commit map
	@NotNull
	public static TreeMap<String, Boolean> getGitHubMap(@NotNull String id) throws Exception {

		// [1] Prepare variables
		TreeMap<String, Boolean> result = new TreeMap<>();
		Connection.Response response = null;

		// [2] GO
		try {
			response = sendHttpRequest(id);						// 1. Send HTTP request
			Document doc = parseHtmlDocument(response.body());	// 2. Parse HTML document
			result = extractTreeMapFromDocument(doc);			// 3. Extract TreeMap from parsed document
		} catch(HttpStatusException e) {
			if(e.getStatusCode() == 404) throw new Exception(pr.l("err_gitHubIDNotExists"));
		} catch(Exception e) {
			throw new Exception();
		} finally {
			// Close HTTP connection
			if(response != null) {
				try {
					response.bodyStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// [3] RETURN RESULTS
		return result;

	}

	// Sends HTTP request and returns response
	private static Connection.Response sendHttpRequest(String id) throws Exception {
		String url = "https://github.com/" + id;
		return Jsoup.connect(url)
				.method(Connection.Method.GET)							// Set request method to GET
				.data("tab", "contributions")					// Add query parameter "tab=contributions"
				.header("x-requested-with", "XMLHttpRequest")	// Add header "x-requested-with: XMLHttpRequest"
				.execute();
	}

	// Parses HTML document and returns Jsoup Document
	private static Document parseHtmlDocument(String html) {
		return Jsoup.parse(html);
	}

	// Extracts TreeMap from parsed HTML document
	private static TreeMap<String, Boolean> extractTreeMapFromDocument(Document doc) {
		TreeMap<String, Boolean> result = new TreeMap<>();
		Elements tdList = doc.select("table.ContributionCalendar-grid td.ContributionCalendar-day");

		// Extract commit map from HTML
		for(Element td: tdList) {
			String keyStr = td.attr("data-date");
			String valStr = td.attr("data-level");
			if (StringUtils.isNumeric(valStr)) {
				String key =  keyStr.substring(0, 4)
							+ keyStr.substring(5, 7)
							+ keyStr.substring(8, 10);
				boolean hasCommit = StringUtils.isNumeric(valStr) && Integer.parseInt(valStr) > 0;
				result.put(key, hasCommit);
			}
		}
		return result;
	}

}