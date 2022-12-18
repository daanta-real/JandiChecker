package cmd;

import crawler.Checker;
import crawler.GithubMap;
import configurations.Configurations;

// 정보 출력 메소드 모음
public class CommandService {

	// 이름을 입력하면 깃헙 ID를 리턴
	public static String getGithubID(String name) {
		for(String[] s: Configurations.getMembers())
			if(s[0].equals(name) || s[0].substring(1).equals(name)) return s[1];
		return null;
	}

	// 이름을 입력하면 종합 커밋정보를 리턴
	public static String showJandiMap(String name) throws Exception {
		String id = getGithubID(name);
		if(id == null) return "없는 스터디원입니다.";
		else return GithubMap.getGithubInfoString(name, id);
	}

	// 특정 Github ID를 입력하면 종합 커밋정보를 리턴
	public static String showJandiMapById(String id) throws Exception {
		return GithubMap.getGithubInfoString("http://github.com/" + id + "/", id);
	}

	// 어제 커밋 안 한 스터디원 목록을 리턴
	public static String showNotCommitedYesterday() throws Exception {
		return Checker.getNotCommittedYesterday();
	}

	// 어제 커밋 한 스터디원 목록을 리턴
	public static String showDidCommitYesterday() throws Exception {
		return Checker.getDidCommitYesterday();
	}

	// 현시각 기준 오늘 아직 커밋 안 한 스터디원 목록을 리턴
	public static String showNotCommitedToday() throws Exception {
		return Checker.getNotCommittedToday();
	}

	// 특정일에 커밋 안 한 스터디원 목록을 리턴
	public static String showNotCommitedSomeday(String date) throws Exception {
		return Checker.getNotCommittedSomeday(date);
	}

}
