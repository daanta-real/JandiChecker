package scheduler;

import crawler.Checker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import jda.JDAMsgService;

import static init.Pr.pr;

// JOB instance actually contains the codes for the job
@Slf4j
public class CronJob implements Job {

	// In the Job class, it is supposed to override the .execute() method and write the codes of the job.
	// This overridden method is executed following the CRON scheduler settings.
	// Formally it seemed that throwing JobExecutionException was required, but seems not anymore.
	@Override
	public void execute(JobExecutionContext context) {
		try {
			String yesterdayCommitedString = Checker.getDidCommitYesterday_onlyIfAny();
			if(StringUtils.equals(yesterdayCommitedString, "")) {
				return;
			}
			log.info("Collected the string of yesterday's commit. total length: {}", yesterdayCommitedString.length());
			JDAMsgService.send(pr.getCronTargetChannelID(), yesterdayCommitedString);
		} catch (Exception e) {
			JDAMsgService.send(pr.getCronTargetChannelID(), pr.l("err_failedToGetInfo"));
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}

}
