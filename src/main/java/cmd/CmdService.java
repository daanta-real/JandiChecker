package cmd;

import crawler.Checker;
import crawler.GitHubMap;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static init.Pr.pr;

// JandiChecker's actual service execution from the user's order starts here.
@Slf4j
public class CmdService {

	private CmdService() {}

	// 1. Summary services

	// Shows the commit summary information of the requested member himself.
	public static String getJandiMapStringOfMine(User user) throws Exception {

		// Get discord name
		String discordName = user.getName();

		// Find info of the member by discord name
		Map<String, String> memberInfo;
		try {
			memberInfo = pr.getMemberInfoesByDiscordName(discordName);
		} catch(Exception e) {
			throw new Exception("Failed to find GitHub ID of %s".formatted(discordName));
		}

		// Return the total information
		try {
			return getJandiMapStringByIdAndName(memberInfo.get("name"), memberInfo.get("gitHubID"));
		} catch(Exception e) {
			throw new Exception();
		}

	}

	// Summary information by GitHub ID
	public static String getJandiMapStringById(String id) {
		if (StringUtils.isEmpty(id)) {
			return pr.l("err_incorrectInput");
		}
		log.info("Received the request the commit map summary of ID {}", id);
		return GitHubMap.getGithubInfoString(id, id);
	}

	// Summary information by GitHub ID and specific member name
	public static String getJandiMapStringByIdAndName(String name, String gitHubID) {
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(gitHubID)) {
			return pr.l("err_incorrectInput");
		}
		log.info("Received the request the commit map summary of GitHub ID {}, NAME {}", gitHubID, name);
		return GitHubMap.getGithubInfoString(name, gitHubID);
	}

	// Summary information by member name
	public static String getJandiMapStringByName(String name) {

		if(StringUtils.isEmpty(name)) {
			return pr.l("cmd_needMemberName");
		}

		String gitHubID;
		try {
			gitHubID = pr.getGitHubIDByMemberName(name);
			if(StringUtils.isEmpty(gitHubID)) throw new Exception();
		} catch(Exception e) {
			return "Failed to find name: %s".formatted(name);
		}

		log.info("Received the request the commit map summary of GitHub ID {}, NAME {}", gitHubID, name);
		return GitHubMap.getGithubInfoString(name, gitHubID);

	}



	// 2. Get list of group members satisfied with specific conditions

	public static String getNotCommittedStringYesterday() throws Exception {
		return Checker.getDidNotCommitYesterday();
	}

	public static String getDidCommitStringYesterday() throws Exception {
		return Checker.getDidCommitYesterday();
	}

	public static String getDidCommitStringToday() {
		return Checker.getDidCommitToday();
	}

	public static String getDidCommitStringSomeday(String date) throws Exception {
		return Checker.getDidCommitSomeday(date);
	}

}
