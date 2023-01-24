package cmd;

import crawler.Checker;
import crawler.GithubMap;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static init.Initializer.LANGUAGE;

// 정보 출력 메소드 모음
@Slf4j
public class CmdService {

	/*
	 * 1. Single jandi map services
	 */

	// 커맨드를 요청한 사람 스스로의 잔디정보를 리턴
	public static String getJandiMapStringOfMe(User user) throws Exception {

		// Get discord tag ID
		String discordTagID = user.getAsTag();

		// Find info of the member by discord tag ID
		Map<String, String> memberInfo;
		try {
			memberInfo = Initializer.getMemberInfoesByDiscordTagID(discordTagID);
		} catch(Exception e) {
			throw new Exception(discordTagID + LANGUAGE.get("cmd_failedToFindGitHubID"));
		}

		// Return the total information
		try {
			return getJandiMapStringByIdAndName(memberInfo.get("name"), memberInfo.get("gitHubID"));
		} catch(Exception e) {
			throw new Exception();
		}

	}

	public static String getJandiMapStringById(String id) { // id로만

		// 미입력 걸러내기
		if (StringUtils.isEmpty(id)) return LANGUAGE.get("plzInputCorrectly");

		// 종합잔디정보 리턴
		log.info(LANGUAGE.get("cmd_getJandiMapStringById"), id);
		return GithubMap.getGithubInfoString(id, id);

	}

	// 특정 이름과 ID의 종합 잔디정보를 리턴
	public static String getJandiMapStringByIdAndName(String name, String id) { // id로만

		// 미입력 걸러내기
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(id)) {
			return LANGUAGE.get("plzInputCorrectly");
		}

		// 종합잔디정보 리턴
		log.info(LANGUAGE.get("cmd_getJandiMapStringByIdAndName"), name, id);
		return GithubMap.getGithubInfoString(name, id);

	}

	public static String getJandiMapStringByName(String name) { // 이름으로만

		// 미입력 걸러내기
		if(StringUtils.isEmpty(name)) {
			return LANGUAGE.get("cmd_needMemberName");
		}

		// 이름이 있으니 ID를 구함
		String gitHubID;
		try {
			gitHubID = Initializer.getGitHubIDByMemberName(name);
			if (StringUtils.isEmpty(gitHubID)) throw new Exception();
		} catch (Exception e) {
			return name + LANGUAGE.get("cmd_failedToFindGitHubID");
		}

		// 이름과 ID로 종합잔디정보 리턴
		log.info(LANGUAGE.get("cmd_getJandiMapStringByName"), name, gitHubID);
		return GithubMap.getGithubInfoString(name, gitHubID);

	}



	/*
	 * 2. Jandi map list services
	 */

	// 어제 잔디심기를 패스한 그룹원 목록을 리턴
	public static String getNotCommittedStringYesterday() throws Exception {
		return Checker.getNotCommittedYesterday();
	}

	// 어제 잔디심기에 성공한 그룹원 목록을 리턴
	public static String getDidCommitStringYesterday() throws Exception {
		return Checker.getDidCommitYesterday();
	}

	// 오늘 잔디심기에 성공한 그룹원 목록을 리턴
	public static String getDidCommitStringToday() {
		return Checker.getDidCommittedToday();
	}

	// 특정일에 잔디심기에 성공한 그룹원 목록을 리턴
	public static String getDidCommitStringSomeday(String date) throws Exception {
		return Checker.getDidCommittedSomeday(date);
	}

}
