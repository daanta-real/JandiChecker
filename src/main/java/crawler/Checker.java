package crawler;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import init.Initializer;
import utils.CommonUtils;

// 깃헙 제출한 사람과 안 한 사람들의 정보를 정리
@Slf4j
public class Checker {

	// id와 특정 일자 문자열(yyyy-MM-dd)을 넣으면 그 사람이 그날 커밋했는지 점검하여 t/f 리턴해줌
	public static boolean getGithubCommittedByDay(String id, String day) throws Exception {
		log.info("{}의 {} 날의 커밋을 확인합니다.", id, day);
		String html_org = Crawler.getHTMLByID(id);
		String trimmed = Crawler.trim(html_org);
		Map<String, Boolean> map = Crawler.makeMapFromTrimmed(trimmed);

		boolean hasCommit = false;
		for(Map.Entry<String, Boolean> entry: map.entrySet()) {
			String key = entry.getKey();
			if(key == null) continue;
			hasCommit = entry.getValue();
			log.info("날짜: " + key + " / 커밋 결과: " + hasCommit);
		}
		return hasCommit;
	}

	// 스터디원 전원의 특정 날의 커밋 현황을 체크하여 커밋한(findNegative true일 시, 안 한) 사람의 명부를 회신
	public static String[] getCommitListByDay(String day, boolean findNegative) throws Exception {
		log.info("커밋을 {} 확인합니다. 확인할 날짜: {}", (findNegative ? "'안 했는지'" : "'했는지'"), day);

		// 날짜 String 만들기
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(String[] s: Initializer.getMembers()) {
			String name = s[0];
			String id = s[1];
			boolean hasCommit = getGithubCommittedByDay(id, day);
			if(
				(!findNegative && hasCommit)    // 커밋한 사람을 찾는 모드일 때, 커밋했을 경우
				|| (findNegative && !hasCommit) // 커밋 빼먹은 사람을 찾는 모드일 때, 커밋 빼멋은 경우
				) {
					count++;
					sb.append(name);
					sb.append("\n");
			}
		}
		return new String[] { String.valueOf(count), sb.toString() };
	}

	// 어제 커밋 안 한 사람의 목록을 회신
	public static String getNotCommittedYesterday() throws Exception {
		log.info("어제 커밋 안 한 사람의 목록을 회신 요청받았습니다.");

		// 날짜 String 만들기
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String day = CommonUtils.sdf.format(c.getTime());

		String[] list = getCommitListByDay(day, true);
		String day_notice = CommonUtils.sdf_dayweek.format(c.getTime());
		return "```md\n[어제 커밋 안 한 사람 " + list[0] + "명]: " + day_notice + "\n" + list[1] + "```";
	}

	// 어제 커밋 한 스터디원 목록을 회신
	public static String getDidCommitYesterday() throws Exception {
		log.info("어제 커밋한 사람의 목록을 회신 요청받았습니다.");

		// 날짜 String 만들기
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String day = CommonUtils.sdf.format(c.getTime());

		String[] list = getCommitListByDay(day, false);
		String day_notice = CommonUtils.sdf_dayweek.format(c.getTime());
		return "```md\n[어제 커밋에 성공한 사람은 " + list[0] + "명...!]: " + day_notice + "\n" + list[1] + "```";
	}

	// 오늘 커밋 안 한 스터디원 목록을 회신
	public static String getDidCommittedToday() throws Exception {

		// 날짜 String 만들기
		Calendar c = Calendar.getInstance();
		String day = CommonUtils.sdf.format(c.getTime());

		String[] list = getCommitListByDay(day, false);
		String day_notice = CommonUtils.sdf_dayweek.format(c.getTime());
		return "```md\n[오늘 커밋에 성공한 사람 " + list[0] + "명 명단]: " + day_notice + "\n" + list[1] + "```";
	}

	// 특정 날짜에 커밋 안 한 스터디원 목록을 회신
	public static String getDidCommittedSomeday(List<String> option) throws Exception {

		// 미입력 걸러내기
		if (option.size() == 0) return "정확히 입력해 주세요.";
		String date = option.get(0);

		// 날짜 양식 불만족 시 리턴
		if(!CommonUtils.isValidDate(date)) return "날짜를 잘못 입력하셨습니다.";
		String[] dates = date.split("-");

		// 날짜 String들 만들기
		Calendar c = CommonUtils.getCalendar(dates[0], (Integer.parseInt(dates[1]) - 1) + "", dates[2]);
		String day = CommonUtils.sdf.format(c.getTime());

		// 본실행
		String[] list = getCommitListByDay(day, false);
		String day_notice = CommonUtils.sdf_dayweek.format(c.getTime());
		return "```md\n[" + date+ "에 커밋에 성공한 사람 " + list[0] + "명 명단]: " + day_notice + "\n" + list[1] + "```";

	}

}
