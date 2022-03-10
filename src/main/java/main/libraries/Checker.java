package main.libraries;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import $.$;
import main.data.MainSystem;
import test.d211003_crawling.Crawler;

// 깃헙 제출한 사람과 안 한 사람들의 정보를 정리
public class Checker {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	// 날짜 유효성 검사
	private static boolean isValidDate(String input) {
        try {
            sdf.setLenient(false);
            sdf.parse(input);
        } catch (Exception e) { return false; }
        return true;
    }

	// id와 특정 일자 문자열(yyyy-MM-dd)을 넣으면 그 사람이 그날 커밋했는지 점검하여 t/f 리턴해줌
	public static boolean getIsGithubCommitedByDay(String id, String day) throws Exception {
		$.pn(id + "의 " + day + " 날의 커밋을 확인합니다.");
		String html = Crawler.makeDataCSV(
				      Crawler.trim(
				      Crawler.getHTML(id)));
		String[] text = html.split("\n");
		boolean resultCommited = false;
		for(String s: text) if(s.substring(0, 10).equals(day)) {
			String[] dayInfo = s.split(",");
			String resultDay = dayInfo[0];
			resultCommited = dayInfo[1].equals("0") ? false : true;
			$.pn("날짜: " + resultDay + " / 커밋 결과: " + resultCommited);
		}
		return resultCommited;
	}

	// 스터디원 전원의 특정 날의 커밋 현황을 체크하여 커밋하지 않은 사람의 명부를 회신
	public static String[] getNotCommitedByDay(String day) throws Exception {
		$.pn("커밋을 확인합니다. 확인할 날짜: " + day);
		StringBuilder sb = new StringBuilder();
		int count = 0;
		MainSystem mainSystem = MainSystem.getInstance();
		for(String[] s: mainSystem.getMembers()) {
			String name = s[0];
			String id = s[1];
			boolean isCommited = getIsGithubCommitedByDay(id, day);
			if(!isCommited) {
				count++;
				sb.append(name + "\n");
			}
		}
		String[] result = { String.valueOf(count), sb.toString() };
		return result;
	}

	// 어제 커밋 안 한 스터디원 목록을 회신
	public static String getNotCommitedYesterday() throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String day = sdf.format(c.getTime());

		String[] list = Checker.getNotCommitedByDay(day);
		SimpleDateFormat sdf_debug = new SimpleDateFormat("yyyy-MM-dd (EEEE)");
		String day_notice = sdf_debug.format(c.getTime());
		String result = "```md\n[어제 커밋 안 한 사람 " + list[0] + "명 명단]: " + day_notice + "\n" + list[1] + "```";
		return result;
	}

	// 오늘 커밋 안 한 스터디원 목록을 회신
	public static String getNotCommitedToday() throws Exception {
		Calendar c = Calendar.getInstance();
		String day = sdf.format(c.getTime());

		String[] list = Checker.getNotCommitedByDay(day);
		SimpleDateFormat sdf_debug = new SimpleDateFormat("yyyy-MM-dd (EEEE)");
		String day_notice = sdf_debug.format(c.getTime());
		String result = "```md\n[오늘 아직 커밋 안 한 사람 " + list[0] + "명 명단]: " + day_notice + "\n" + list[1] + "```";
		return result;
	}

	// 특정 날짜에 커밋 안 한 스터디원 목록을 회신
	public static String getNotCommitedSomeday(String date) throws Exception {
		// 날짜 양식 불만족 시 리턴
		if(!isValidDate(date)) return "날짜를 잘못 입력하셨습니다.";
		String[] dates = date.split("-");

		// 날짜 String들 만들기
		Calendar c = $.getCalendar(dates[0], (Integer.valueOf(dates[1]) - 1) + "", dates[2]);
		String day = sdf.format(c.getTime());

		// 본실행
		String[] list = Checker.getNotCommitedByDay(day);
		SimpleDateFormat sdf_debug = new SimpleDateFormat("yyyy-MM-dd (EEEE)");
		String day_notice = sdf_debug.format(c.getTime());
		String result = "```md\n[특정 날짜에 커밋 안 한 사람 " + list[0] + "명 명단]: " + day_notice + "\n" + list[1] + "```";
		return result;
	}

}
