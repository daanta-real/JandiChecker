package scheduler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import cmd.CmdService;
import jda.JDAMsgService;

import static init.Pr.pr;

// JOB instance actually contains the codes for the job
@Slf4j
public class CronJob implements Job {

	// In the Job class, it is supposed to after overriding the .execute() method and write the codes of the job.
	// This override method is executed following the CRON scheduler settings.
	// Originally it is needed to attach throwing of JobExecutionException, but this seems to be changed as not throw.
	@Override
	public void execute(JobExecutionContext context) {
		try {
			String yesterdayCommitedString = CmdService.getDidCommitStringYesterday();
			log.info("Collected the string of yesterday's commit. total length: {}", yesterdayCommitedString.length());
			JDAMsgService.send(pr.getCronTargetChannelID(), yesterdayCommitedString);
		} catch (Exception e) {
			JDAMsgService.send(pr.getCronTargetChannelID(), pr.l("err_failedToGetInfo"));
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}

}
