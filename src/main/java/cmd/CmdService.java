package cmd;

import crawler.Checker;
import crawler.GithubMap;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static init.Initializer.pr;

// JandiChecker's actual service execution from the user's order starts here.
@Slf4j
public class CmdService {

	// 1. Summary services

	// Shows the commit summary information of the requested member himself.
	public static String getJandiMapStringOfMine(User user) throws Exception {

		// Get discord tag ID
		String discordTagID = user.getAsTag();

		// Find info of the member by discord tag ID
		Map<String, String> memberInfo;
		try {
			memberInfo = pr.getMemberInfoesByDiscordTagID(discordTagID);
		} catch(Exception e) {
			throw new Exception(discordTagID + pr.l("cmd_failedToFindGitHubID"));
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
		log.info(pr.l("cmd_getJandiMapStringById"), id);
		return GithubMap.getGithubInfoString(id, id);
	}

	// Summary information by GitHub ID and specific member name
	public static String getJandiMapStringByIdAndName(String name, String id) {
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(id)) {
			return pr.l("err_incorrectInput");
		}
		log.info(pr.l("cmd_getJandiMapStringByIdAndName"), name, id);
		return GithubMap.getGithubInfoString(name, id);
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
			return name + pr.l("cmd_failedToFindGitHubID");
		}

		log.info(pr.l("cmd_getJandiMapStringByName"), name, gitHubID);
		return GithubMap.getGithubInfoString(name, gitHubID);

	}



	// 2. Get list of group members satisfied with specific conditions

	public static String getNotCommittedStringYesterday() throws Exception {
		return Checker.getNotCommittedYesterday();
	}

	public static String getDidCommitStringYesterday() throws Exception {
		return Checker.getDidCommitYesterday();
	}

	public static String getDidCommitStringToday() {
		return Checker.getDidCommittedToday();
	}

	public static String getDidCommitStringSomeday(String date) throws Exception {
		return Checker.getDidCommittedSomeday(date);
	}

}
