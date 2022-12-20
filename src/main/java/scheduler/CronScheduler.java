package scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import configurations.Configurations;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class CronScheduler {

	// 스케쥴러 생성과 실행을 담당하는 메소드
	public static void scheduleExecute(Class<? extends Job> jobClass, String cron) throws Exception {

		// 스케쥴러 객체 생성: 팩토리와 인스턴스
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();

		// 설정 객체 생성: 실행할 job 객체, 실행 주기 trigger 객체
		JobDetail job = newJob(jobClass)
			.withIdentity("jobName", org.quartz.Scheduler.DEFAULT_GROUP)
			.build();
		Trigger trigger = newTrigger()
			.withIdentity("trggerName", org.quartz.Scheduler.DEFAULT_GROUP)
			.withSchedule(cronSchedule(cron))
			.build();

		// 위에서 생성한 설정 객체인 job 객체와 trigger 객체를 스케쥴러에 장전시킴
		scheduler.scheduleJob(job, trigger);

		// 발사명령. 이제 외부에서 CronScheduler.run() 외치면 여기까지 실행되고 스케쥴러가 실행된다.
		scheduler.start();

	}

	// 스케쥴러 실행
	public static void run() throws Exception {
		scheduleExecute(CronJob.class, Configurations.getCron());
	}

}
