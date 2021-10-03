package main.libraries;

import main.data.DataSystem;

public class Cmd {

	// 1명의 깃헙 ID를 획득
	public static String getGithubID(String name) {
		for(String[] s: DataSystem.LIST)
			if(s[0].equals(name) || s[0].substring(1).equals(name)) return s[1];
		return null;
	}

	// 1명의 깃헙 정보를 출력
	public static String showJandiMap(String name) throws Exception {
		String id = getGithubID(name);
		return GithubMap.getGithubInfoString(name, id);
	}

	// 어제 커밋 안 한 스터디원 목록을 출력
	public static String showNotCommitedYesterday() throws Exception {
		return Checker.getNotCommitedYesterday();
	}

	// 지금 커밋 안 한 스터디원 목록을 출력
	public static String showNotCommitedToday() throws Exception {
		return Checker.getNotCommitedToday();
	}

	// 특정일에 커밋 안 한 스터디원 목록을 출력
	public static String showNotCommitedSomeday(String date) throws Exception {
		return Checker.getNotCommitedSomeday(date);
	}
}