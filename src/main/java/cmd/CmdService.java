package cmd;

import crawler.Checker;
import crawler.GithubMap;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.lang3.StringUtils;

// 정보 출력 메소드 모음
@Slf4j
public class CmdService {

	/*
	 * 0. Libraries
	 */

	// 이름을 입력하면 깃헙 ID를 리턴
	public static String getGitHubIDByGroupName(String name) {
		for(String[] s: Initializer.getMembers())
			if(s[0].equals(name) || s[0].substring(1).equals(name)) return s[1];
		return null;
	}



	/*
	 * 1. Single jandi map services
	 */

	// 커맨드를 요청한 사람의 잔디정보를 리턴
	public static String showJandiMapOfMe(SlashCommandInteractionEvent event) {

		// ID 구하기
		User user = event.getUser();
		String name = user.getName();
		String eventDiscordID = user.getAsTag();
		String gitHubID = null;
		for(String[] member: Initializer.getMembers()) {
			if(member.length < 3) continue;
			String yamlDiscordID = member[2];
			if(eventDiscordID.equals(yamlDiscordID)) {
				String memberGitHubID = member[1];
				if(memberGitHubID != null) gitHubID = memberGitHubID;
			}
		}
		if(StringUtils.isEmpty(gitHubID)) return name + "님의 GitHub ID를 찾는 데 실패했습니다.";

		// 종합잔디정보 리턴
		return showJandiMapById(gitHubID);

	}

	// 특정 ID의 종합 잔디정보를 리턴
	public static String showJandiMapById(String id) { // id로만

		// 미입력 걸러내기
		if (StringUtils.isEmpty(id)) return "정확히 입력해 주세요.";

		// 종합잔디정보 리턴
		log.info("ID '{}'의 종합 잔디정보 호출을 명령받았습니다.", id);
		return GithubMap.getGithubInfoString(id, id);

	}

	// 특정 그룹원 이름으로 종합 잔디정보를 리턴
	public static String showJandiMapByName(String name) { // id로만

		// 미입력 걸러내기
		if(StringUtils.isEmpty(name)) return "찾고자 하는 그룹원 이름을 입력해 주세요.";

		// ID 구하기
		String id = getGitHubIDByGroupName(name);
		if(StringUtils.isEmpty(id)) return "해당 이름으로 그룹원을 찾지 못하였습니다.";
		
		// 종합잔디정보 리턴
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

	// 특정일에 잔디심기에 성공한 그룹원 목록을 리턴
	public static String showDidCommitToday() throws Exception {
		return Checker.getDidCommittedToday();
	}

	// 특정일에 잔디심기에 성공한 그룹원 목록을 리턴
	public static String showDidCommitSomeday(String date) throws Exception {
		return Checker.getDidCommittedSomeday(date);
	}

}
