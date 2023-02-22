package crawler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import static init.Pr.pr;
import static utils.CommonUtils.getCalendar;


// Extract specific data gathered with Crawler, or return info about some member's GitHub repo
@Slf4j
public class GithubMap {

	// Returns date Strings of given day
	private static Calendar getDate(String dateStr) {
		String y = dateStr.substring(0, 4);
		String m = String.valueOf(Integer.parseInt(dateStr.substring(4, 6)) - 1);
		String d = dateStr.substring(6, 8);
		return getCalendar(y, m, d);
	}

	// Get total commit information as Map by GitHub ID
	public static TreeMap<String, Object> getGithubMapInfo(String id) throws Exception {

		// 1. Definitions
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd (EEEE)");
		StringBuilder[] sb = new StringBuilder[7];
		for(int i = 0; i < 7; i++) sb[i] = new StringBuilder();

		// 2. Get whole commit map
		TreeMap<String, Boolean> map;
		try {
			map = Crawler.getGithubMap(id);
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}

		// 3. Calc first and last day of the map
		Calendar firstDay = getDate(Collections.min(map.keySet()));
		Calendar lastDay  = getDate(Collections.max(map.keySet()));
		String periodStr = pr.l("period") + ": {} ~ {}";
		log.info(periodStr, sdf.format(firstDay.getTime()), sdf.format(lastDay.getTime()));

		// 4. Calculate the first Sunday of the week of the day of the first commit occur.
		//    Prepare the map String arrays, put dots from that Sunday to the day before the first day.
		int move = -firstDay.get(Calendar.DAY_OF_WEEK) + 1; // amount needed to go back to sunday
		for(int i = 0; i < move; i++) sb[move].append('.');
		String firstSundayStr = pr.l("gitHubMap_firstSunday") + ", sb[0] = {}";
		log.info(firstSundayStr, move, sb[0].toString());

		// 5. Make map data
		Calendar cal;
		int count = 0;
		boolean[] commitTFs = new boolean[500]; // Stores success status of commits (from the first day to the end)
		for(Map.Entry<String, Boolean> entry: map.entrySet()) {

			// Definitions
			String k = entry.getKey();
			cal = getDate(k);
			int weekday = count % 7;

			// Record
			boolean commitSuccess = entry.getValue();
			char text = commitSuccess ? '●' : '○';
			sb[weekday].append(text); // detailed Strings
			commitTFs[count] = commitSuccess; // detailed T/F

			// Clean
			log.info("FOUND DATE ({}th, %7 = {}): {} > {}", count, weekday, sdf.format(cal.getTime()), map.get(k));
			count++;

		}

		// Result
		TreeMap<String, Object> result = new TreeMap<>();

		// Integrate whole map data into one String
		StringBuilder sbResult = new StringBuilder();
		for(StringBuilder s: sb) {
			sbResult.append(s.toString());
			sbResult.append("\n");
		}
		result.put("totalMap", sbResult.toString());
		String mapInfo = pr.l("gitHubMap_mapInfo") + ":\n{}";
		log.info(mapInfo, sbResult);

		// Get count of committed / not committed in last 2 weeks
		int recentCount = 0, recentTotal = 0;
		for(int i = count - 30; i < count; i++) {
			recentTotal++;
			log.info("DAY {} -> commit T/F: {}", i, commitTFs[i]);
			recentCount += commitTFs[i] ? 1 : 0;
		}
		result.put("recentTotal", recentTotal);
		result.put("recentCount", recentCount);

		// Done!
		return result;

	}

	// Returns commit information of specific member name
	public static String getGithubInfoString(String name, String id) {

		TreeMap<String, Object> map;
		try {
			map = getGithubMapInfo(id);
		} catch(Exception e) {
			log.error(e.getMessage());
			return e.getMessage();
		}

		String emoji = "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93";
		List<String> result = new ArrayList<>();

		result.add(
				"%s %s%s(<http://github.com/%s>)%s %s\n```md"
				.formatted(emoji, name, pr.l("gitHubMap_sirStr"), id, pr.l("gitHubMap_mapOf"), emoji)
		);

		int count = (int) map.get("recentCount");
		float total = (int) map.get("recentTotal");
		float percOrg = count / total * 100;
		String perc = new DecimalFormat("#.##").format(percOrg);
		log.info("Committed days in recent 30 days: {}", count);
		log.info("Total days count in 30 days: {}", total);
		log.info("Committed rate in 30 days: {}" + perc);
		result.add(
				"# %s: %s%s %s%%)".formatted(
						pr.l("gitHubMap_CommittedInLast30"),
						count,
						pr.l("gitHubMap_count"),
						perc)
		);

		result.add("# " + pr.l("gitHubMap_CommitMapOfRecent1Year"));

		result.add(String.valueOf(map.get("totalMap")));

		result.add("```");

		return String.join("\n", result);

	}

}
