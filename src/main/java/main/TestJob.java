package main;

import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		BotMain.send(Calendar.getInstance().get(Calendar.YEAR) + "년이 밝았습니다. 새해 복 많이 받으세요. ※ age++;");
	}

}