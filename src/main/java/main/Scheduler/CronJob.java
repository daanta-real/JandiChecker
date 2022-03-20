package main.Scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import main.Cmd.Commands;
import main.Cmd.Sender;
import main.Settings.MainSettings;

// 실제 실행될 JOB 내용만을 담고 있는 실행내용 객체
public class CronJob implements Job {

	// JOB 객체에서는 execute를 오버라이드하여 그 작업내용을 쓰도록 되어 있다.
	// CRON 스케쥴러 설정주기에 맞게 이 메소드가 실행된다.
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String yesterdayCommitedString = Commands.showDidCommitYesterday();
			Sender.send(MainSettings.getChId(), yesterdayCommitedString);
		} catch (Exception e) { e.printStackTrace(); }
	}

}
