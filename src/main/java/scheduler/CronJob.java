package scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import cmd.CommandExecute;
import jda.JdaMsgSender;
import configurations.Configurations;

// 실제 실행될 JOB 내용만을 담고 있는 실행내용 객체
public class CronJob implements Job {

	// JOB 객체에서는 execute를 오버라이드하여 그 작업내용을 쓰도록 되어 있다.
	// CRON 스케쥴러 설정주기에 맞게 이 메소드가 실행된다.
	// 구버전에서는 JobExecutionException을 throw해야 했었지만 업데이트되면서 바뀐 듯하다.
	@Override
	public void execute(JobExecutionContext context) {
		try {
			String yesterdayCommitedString = CommandExecute.showDidCommitYesterday();
			JdaMsgSender.send(Configurations.getChId(), yesterdayCommitedString);
		} catch (Exception e) { e.printStackTrace(); }
	}

}
