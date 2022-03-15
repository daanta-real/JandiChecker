package main.Scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import $.$;

// 실제 실행될 JOB 내용만을 담고 있는 실행내용 객체
public class CronJob implements Job {

	// JOB 객체에서는 execute를 오버라이드하여 그 작업내용을 쓰도록 되어 있다.
	// CRON 스케쥴러 설정주기에 맞게 이 메소드가 실행된다.
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		$.pn("예약 작업 실행됨. 현재 시각: " + new Date());
	}

}
