package crawler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import static init.Initializer.props;
import static utils.CommonUtils.getCalendar;

// 크롤러에서 읽어온 데이터에서 특정 정보를 빼내오거나, 특정 인원들의 깃헙 정보를 출력해줌
@Slf4j
public class GithubMap {

	// 특정 날짜의 객체 획득
	private static Calendar getDate(String dateStr) {
		String y = dateStr.substring(0, 4);
		String m = String.valueOf(Integer.parseInt(dateStr.substring(4, 6)) - 1);
		String d = dateStr.substring(6, 8);
		return getCalendar(y, m, d);
	}

	// 깃헙 잔디 정보 획득
	public static TreeMap<String, Object> getGithubMapInfo(String id) throws Exception {

		// 변수정의
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd (EEEE)");
		StringBuilder[] sb = new StringBuilder[7];
		for(int i = 0; i < 7; i++) sb[i] = new StringBuilder();

		// 맵 갖고 오기
		TreeMap<String, Boolean> map;
		try {
			map = Crawler.getGithubMap(id);
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}

		// 첫 날과 마지막 날을 구함
		Calendar firstDay = getDate(Collections.min(map.keySet()));
		Calendar lastDay  = getDate(Collections.max(map.keySet()));
		String periodStr = props.lang("period") + ": {} ~ {}";
		log.info(periodStr, sdf.format(firstDay.getTime()), sdf.format(lastDay.getTime()));

		// 첫 원소의 일요일 날짜부터 시작일 직전일까지 필요한 칸수를 확인하여 점을 다 찍어줌
		int move = -firstDay.get(Calendar.DAY_OF_WEEK) + 1; // amount needed to go back to sunday
		for(int i = 0; i < move; i++) sb[move].append('.');
		String firstSundayStr = props.lang("gitHubMap_firstSunday") + ", sb[0] = {}";
		log.info(firstSundayStr, move, sb[0].toString());

		// 데이터 만들기
		Calendar cal;
		int count = 0;
		boolean[] commitTFs = new boolean[500]; // Stores success status of commits (from the first day to the end)
		for(Map.Entry<String, Boolean> entry: map.entrySet()) {

			// 변수준비
			String k = entry.getKey();
			cal = getDate(k);
			int weekday = count % 7;

			// 기록
			boolean commitSuccess = entry.getValue();
			char text = commitSuccess ? '●' : '○';
			sb[weekday].append(text); // 상세현황 문자열
			commitTFs[count] = commitSuccess; // 상세현황 tf

			// 정리
			log.info("FOUND DATE ({}th, %7 = {}): {} > {}", count, weekday, sdf.format(cal.getTime()), map.get(k));
			count++;

		}



		// 결과부
		// 결과 변수
		TreeMap<String, Object> result = new TreeMap<>();

		// 전체맵
		StringBuilder sbResult = new StringBuilder();
		for(StringBuilder s: sb) {
			sbResult.append(s.toString());
			sbResult.append("\n");
		}
		result.put("totalMap", sbResult.toString());
		String mapInfo = props.lang("gitHubMap_mapInfo") + ":\n{}";
		log.info(mapInfo, sbResult);

		// 최근 2주 간의 TF
		int recentCount = 0, recentTotal = 0;
		for(int i = count - 30; i < count; i++) {
			recentTotal++;
			log.info("DAY {} -> commit T/F: {}", i, commitTFs[i]);
			recentCount += commitTFs[i] ? 1 : 0;
		}
		result.put("recentTotal", recentTotal);
		result.put("recentCount", recentCount);

		// 결과 변수 리턴
		return result;

	}

	// 특정 인원에 대한 잔디 정보를 최종 구성해서 회신
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
				.formatted(emoji, name, props.lang("gitHubMap_sirStr"), id, props.lang("gitHubMap_mapOf"), emoji)
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
						props.lang("gitHubMap_CommittedInLast30"), count,
						props.lang("gitHubMap_count"), perc)
		);

		result.add("# " + props.lang("gitHubMap_CommitMapOfRecent1Year"));

		result.add(String.valueOf(map.get("totalMap")));

		result.add("```");

		return String.join("\n", result);

	}

}
