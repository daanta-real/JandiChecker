package crawler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import utils.CommonUtils;

import static init.Pr.pr;

// Check and get the list of the members who committed or not in specific day
@Slf4j
public class Checker {

	// Input the target GitHub ID and date(format: yyyy-MM-dd) and get a T/F where he committed or not
	public static boolean getGitHubDidCommitByDay(String id, String day) throws Exception {

		// 1. Preparing
		log.info("CHECK {}'s COMMIT. DATE: {}", id, day);

		// 2. Get full HTML
		String html_org;
		try {
			html_org = Crawler.getHTMLByID(id);
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}

		// 3. HTML to Map
		Map<String, Boolean> map = Crawler.makeMapFromTrimmed(html_org);
		if(map.size() == 0) throw new Exception();

		// 4. Check jandi result.
		// If commit box is not found here throws an Exception
		if(map.get(day) == null) throw new Exception();
		boolean committed = map.get(day);
		log.debug("DATE = {} → SCORE = {}", day, committed);

		// 5. Return result
		return committed;

	}

	// Check the all members and returns the count and the list who DID or DID NOT commit
	// Returns raw String array, not for the chat
	// * if findNegative is true then only find DID list
	// * if findNegative is true then only find NOT DID list
	public static String[] getCommitListByDay(String day, boolean findNegative) throws Exception {

		log.info(
				"Check if he '{}' of the day: {}",
				(findNegative
						? "DIDN'T COMMIT"
						: "DID COMMIT"), day);

		// result values
		StringBuilder sb = new StringBuilder(); // name list (one name by one line)

		Map<String, Map<String, String>> members = pr.getMembers();

		List<String> keyList = new ArrayList<>(members.keySet());
		int countTotal = keyList.size();
		int countMeet = 0;

		for(int i = 0; i < keyList.size(); i++) {

			String name = keyList.get(i);
			Map<String, String> memberProps = members.get(name);

			String gitHubID = memberProps.get("gitHubID");
			boolean hasCommit = getGitHubDidCommitByDay(gitHubID, day);
			log.info("checking {} / {} (case met: {})", i, countTotal, countMeet);
			if(
				(!findNegative && hasCommit)    // find only DID committed     and he DID commit
				|| (findNegative && !hasCommit) // find only DID NOT committed and he DID NOT commit
				) {
					countMeet++;
					sb.append(name);
					sb.append("\n");
			}
		}
		return new String[] { String.valueOf(countMeet), sb.toString() };

	}

	// Check the all members and returns the list who DID NOT commit yesterday
	public static String getDidNotCommitYesterday() throws Exception {

		log.info("Request: get list of NOT committed yesterday");

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String day = CommonUtils.sdf_thin.format(c.getTime());

		String[] list = getCommitListByDay(day, true);
		String date_notice = CommonUtils.sdf_dayweek.format(c.getTime());
		String result = "```md\n[" + pr.l("checker_getNotCommittedYesterday_result") + "]: %s\n%s\n```";
		return result.formatted(list[0], date_notice, list[1]);

	}

	// Check the all members and returns the list who DID commit yesterday
	public static String getDidCommitYesterday() throws Exception {

		log.info("Request: get list of DID committed yesterday");

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String day = CommonUtils.sdf_thin.format(c.getTime());

		String[] list = getCommitListByDay(day, false);
		String date_notice = CommonUtils.sdf_dayweek.format(c.getTime());
		String result = "```md\n[" + pr.l("checker_getDidCommitYesterday_result") + "]: %s\n%s\n```";
		return result.formatted(list[0], date_notice, list[1]);

	}

	// Check the all members and returns the list who DID commit today
	// maybe it gets an error before 6 o'clock
	public static String getDidCommitToday() {

		log.info("Request: get list of DID commit TODAY");

		try {
			Calendar c = Calendar.getInstance();
			String day = CommonUtils.sdf_thin.format(c.getTime());

			String date_notice = CommonUtils.sdf_dayweek.format(new Date());
			String[] list = getCommitListByDay(day, false);
			String result = "```md\n[" + pr.l("checker_getDidCommittedToday_result_success") + "]: %s\n%s\n```";
			return result.formatted(list[0], date_notice, list[1]);
		} catch(Exception e) {
			return "%s\n```md\n%s```".formatted(
					pr.l("err_failedToGetInfo"),
					pr.l("checker_getDidCommittedToday_result_fail_description")
			);
		}

	}

	// Check the all members and returns the list who DID commit in specific day
	public static String getDidCommitSomeday(String date) throws Exception {

		// chksum: null check, date String format
		if (StringUtils.isEmpty(date)) {
			return pr.l("err_incorrectInput");
		}
		String date_today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		if(!CommonUtils.isValidDate(date)) {
			return pr.l("err_dateStr");
		}
		if(date_today.compareTo(date) < 0) {
			return pr.l("err_dateValue"); // if date's day is future then this is -1 so can't satisfy
		}

		// Calendar instance, and date String of picked day
		String y = date.substring(0, 4);
		String m = String.valueOf(Integer.parseInt(date.substring(4, 6)) - 1);
		String d = date.substring(6, 8);
		Calendar c = CommonUtils.getCalendar(y, m, d);
		String day = CommonUtils.sdf_thin.format(c.getTime());

		try {
			String[] list = getCommitListByDay(day, false);
			String day_notice = CommonUtils.sdf_dayweek.format(c.getTime());
			String result = "```md" +
					"[" + pr.l("checker_getDidCommittedSomeday") + "]: %s" +
					"%s```";
			return result.formatted(day_notice, list[0], day_notice, list[1]);
		} catch(Exception e) {
			Calendar todayCalendar = Calendar.getInstance();
			String todayStr = CommonUtils.sdf_thin.format(todayCalendar.getTime());
			if(todayStr.equals(date)) {
				return "%s\n```md\n%s```".formatted(
						pr.l("err_failedToGetInfo"),
						pr.l("checker_getDidCommittedToday_result_fail_description"));
			} else {
				throw new Exception();
			}
		}

	}

}
