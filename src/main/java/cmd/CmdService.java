package cmd;

import crawler.Checker;
import crawler.GithubMap;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

// 정보 출력 메소드 모음
@Slf4j
public class CmdService {

	// 이름을 입력하면 깃헙 ID를 리턴
	public static String getGithubID(String name) {
		for(String[] s: Initializer.getMembers())
			if(s[0].equals(name) || s[0].substring(1).equals(name)) return s[1];
		return null;
	}

	// 이름을 입력하면 종합 커밋정보를 리턴
	public static String showJandiMap(List<String> option) throws Exception {

		// 미입력 걸러내기
		if (option.size() == 0) return "정확히 입력해 주세요.";

		// 이름, ID 구하기
		String name = option.get(0);
		String id = getGithubID(name);
		if(id == null) return "없는 스터디원입니다.";

		// 종합커밋정보 리턴
		log.info("{}님 (ID: {})의 정보 호출을 명령받았습니다.", name, id);
		return GithubMap.getGithubInfoString(name, id);

	}

	// 특정 Github ID를 입력하면 종합 커밋정보를 리턴
	public static String showJandiMapById(List<String> option) throws Exception {

		// 미입력 걸러내기
		if (option.size() == 0) return "정확히 입력해 주세요.";

		// ID 구하기
		String id = option.get(0);

		return GithubMap.getGithubInfoString(id, id);
	}

	// 어제 커밋 안 한 스터디원 목록을 리턴
	public static String showNotCommittedYesterday() throws Exception {
		return Checker.getNotCommittedYesterday();
	}

	// 어제 커밋 한 스터디원 목록을 리턴
	public static String showDidCommitYesterday() throws Exception {
		return Checker.getDidCommitYesterday();
	}

	// 현시각 기준 오늘 아직 커밋 한 스터디원 목록을 리턴
	public static String showDidCommitToday() throws Exception {
		return Checker.getDidCommittedToday();
	}

	// 특정일에 커밋 한 스터디원 목록을 리턴
	public static String showDidCommitSomeday(List<String> option) throws Exception {
		return Checker.getDidCommittedSomeday(option);
	}

}
