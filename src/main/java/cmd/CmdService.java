package cmd;

import crawler.Checker;
import crawler.GithubMap;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

// 정보 출력 메소드 모음
@Slf4j
public class CmdService {

	/*
	 * 1. Single jandi map services
	 */

	// 커맨드를 요청한 사람 스스로의 잔디정보를 리턴
	public static String showJandiMapOfMe(User user) {

		// 이름과 ID 구하기
		String name = null;
		String discordID = user.getAsTag();
		Map<String, String> memberInfo;
		try {
			memberInfo = Initializer.getMemberInfoesByDiscordID(discordID);
		} catch(Exception e) {
			return name + "님의 GitHub ID를 찾는 데 실패했습니다.";
		}

		// 종합잔디정보 리턴
		return showJandiMapByIdAndName(memberInfo.get("name"), memberInfo.get("gitHubID"));

	}

	public static String showJandiMapById(String id) { // id로만

		// 미입력 걸러내기
		if (StringUtils.isEmpty(id)) return "정확히 입력해 주세요.";

		// 종합잔디정보 리턴
		log.info("ID '{}'의 종합 잔디정보 호출을 명령받았습니다.", id);
		return GithubMap.getGithubInfoString(id, id);

	}

	// 특정 이름과 ID의 종합 잔디정보를 리턴
	public static String showJandiMapByIdAndName(String name, String id) { // id로만

		// 미입력 걸러내기
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(id)) return "정확히 입력해 주세요.";

		// 종합잔디정보 리턴
		log.info("'{}'님 (ID '{}')의 종합 잔디정보 호출을 명령받았습니다.", name, id);
		return GithubMap.getGithubInfoString(name, id);

	}

	public static String showJandiMapByName(String name) { // 이름으로만

		// 미입력 걸러내기
		if(StringUtils.isEmpty(name)) return "찾고자 하는 그룹원 이름을 입력해 주세요.";

		// 이름이 있으니 ID를 구함
		String id;
		try {
			id = Initializer.getGitHubIDByMemberName(name);
			if (StringUtils.isEmpty(id)) throw new Exception();
		} catch (Exception e) {
			return "해당 이름으로 그룹원을 찾지 못하였습니다.";
		}

		// 이름과 ID로 종합잔디정보 리턴
		log.info("그룹원 '{}' (ID '{}')의 종합 잔디정보 호출을 명령받았습니다.", name, id);
		return GithubMap.getGithubInfoString(name, id);

	}



	/*
	 * 2. Jandi map list services
	 */

	// 어제 잔디심기를 패스한 그룹원 목록을 리턴
	public static String showNotCommittedYesterday() throws Exception {
		return Checker.getNotCommittedYesterday();
	}

	// 어제 잔디심기에 성공한 그룹원 목록을 리턴
	public static String showDidCommitYesterday() throws Exception {
		return Checker.getDidCommitYesterday();
	}

	// 오늘 잔디심기에 성공한 그룹원 목록을 리턴
	public static String showDidCommitToday() {
		return Checker.getDidCommittedToday();
	}

	// 특정일에 잔디심기에 성공한 그룹원 목록을 리턴
	public static String showDidCommitSomeday(String date) throws Exception {
		return Checker.getDidCommittedSomeday(date);
	}

}
