package crawler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import utils.CommonUtils;

import static init.Initializer.props;

// 깃헙 제출한 사람과 안 한 사람들의 정보를 정리
@Slf4j
public class Checker {

	// id와 특정 일자 문자열(yyyy-MM-dd)을 넣으면 그 사람이 그날 잔디를 심었는지를 true/false 로 리턴해줌
	public static boolean getGithubCommittedByDay(String id, String day) throws Exception {

		// 1. Preparing
		log.info(props.lang("checker_getGithubCommittedByDay"), id, day);

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
		log.debug(props.lang("checker_scoreOfDay"), day, committed);

		// 5. Return result
		return committed;

	}

	// 그룹원 전원의 특정 날의 잔디심기 현황을 체크하여 (findNegative true일 시, 패스한) 사람의 명부를 회신
	public static String[] getCommitListByDay(String day, boolean findNegative) throws Exception {
		log.info(props.lang("checker_getCommitListByDay"), (findNegative ? "안 심었는지" : "심었는지"), day);

		// 날짜 String 만들기
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(Map.Entry<String, Map<String, String>> entry: props.getMembers().entrySet()) {
			Map<String, String> memberProps = entry.getValue();
			String name = entry.getKey();
			String gitHubID = memberProps.get("gitHubID");
			boolean hasCommit = getGithubCommittedByDay(gitHubID, day);
			if(
				(!findNegative && hasCommit)    // 잔디 심은 사람을 찾는 모드일 때, 심은 경우
				|| (findNegative && !hasCommit) // 잔디를 심지 않는 사람을 찾는 모드일 때, 심지 않은 경우
				) {
					count++;
					sb.append(name);
					sb.append("\n");
			}
		}
		return new String[] { String.valueOf(count), sb.toString() };
	}

	// 어제 잔디를 심지 않은 사람의 목록을 회신
	public static String getNotCommittedYesterday() throws Exception {

		log.info(props.lang("checker_getNotCommittedYesterday"));

		// 날짜 String 만들기
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String day = CommonUtils.sdf_thin.format(c.getTime());

		String[] list = getCommitListByDay(day, true);
		String date_notice = CommonUtils.sdf_dayweek.format(c.getTime());
		String result = "```md\n[" + props.lang("checker_getNotCommittedYesterday_result") + "]: %s\n%s\n```";
		return result.formatted(list[0], date_notice, list[1]);

	}

	// 어제 잔디를 심은 그룹원 목록을 회신
	public static String getDidCommitYesterday() throws Exception {

		log.info(props.lang("checker_getDidCommitYesterday"));

		// 날짜 String 만들기
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String day = CommonUtils.sdf_thin.format(c.getTime());

		String[] list = getCommitListByDay(day, false);
		String date_notice = CommonUtils.sdf_dayweek.format(c.getTime());
		String result = "```md\n[" + props.lang("checker_getDidCommitYesterday_result") + "]: %s\n%s\n```";
		return result.formatted(list[0], date_notice, list[1]);

	}

	// 오늘 잔디를 심은 그룹원 목록을 회신
	public static String getDidCommittedToday() {

		log.info(props.lang("checker_getDidCommittedToday"));

		try {
			// 날짜 String 만들기
			Calendar c = Calendar.getInstance();
			String day = CommonUtils.sdf_thin.format(c.getTime());

			String date_notice = CommonUtils.sdf_dayweek.format(new Date());
			String[] list = getCommitListByDay(day, false);
			String result = "```md\n[" + props.lang("checker_getDidCommittedToday_result_success") + "]: %s\n%s\n```";
			return result.formatted(list[0], date_notice, list[1]);
		} catch(Exception e) {
			return "%s\n```md\n%s```".formatted(
					props.lang("checker_getDidCommittedToday_result_fail_title"),
					props.lang("checker_getDidCommittedToday_result_fail_md")
			);
		}

	}

	// 특정 날짜에 잔디를 심은 그룹원 목록을 회신
	public static String getDidCommittedSomeday(String date) throws Exception {

		// 미입력 걸러내기
		if (StringUtils.isEmpty(date)) return props.lang("err_incorrectInput");

		// 값 검사: 날짜 양식 불만족 시 리턴
		String date_today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		if(!CommonUtils.isValidDate(date)) return props.lang("err_dateStr");
		if(date_today.compareTo(date) < 0) return props.lang("err_dateValue"); // date가 현재보다 미래 날짜일 경우 -1이 되어 불만족

		// 연, 월, 일 문자열 만들어 날짜 객체 구하기
		String y = date.substring(0, 4);
		String m = String.valueOf(Integer.parseInt(date.substring(4, 6)) - 1);
		String d = date.substring(6, 8);
		Calendar c = CommonUtils.getCalendar(y, m, d);

		// 조회하고자 하는 날짜 문자열 만들기
		String day = CommonUtils.sdf_thin.format(c.getTime());

		// 본실행
		try {
			String[] list = getCommitListByDay(day, false);
			String day_notice = CommonUtils.sdf_dayweek.format(c.getTime());
			String result = "```md\n[" + props.lang("checker_getDidCommittedSomeday") + "]: %s\n%s```";
			return result.formatted(day_notice, list[0], day_notice, list[1]);
		} catch(Exception e) {
			Calendar todayCalendar = Calendar.getInstance();
			String todayStr = CommonUtils.sdf_thin.format(todayCalendar.getTime());
			if(todayStr.equals(date)) {
				return "%s\n```md\n%s```".formatted(
						props.lang("checker_getDidCommittedToday_result_fail_title"),
						props.lang("checker_getDidCommittedToday_result_fail_md"));
			} else {
				throw new Exception();
			}
		}

	}

}
