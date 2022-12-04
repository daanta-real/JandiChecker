package test.d220103_Scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;


@Slf4j
public class Scheduler {

	public static void run(Class<? extends Job> jobClass, String cron) {

	    // 스케쥴러 팩토리 생성
	    SchedulerFactory schedulerFactory = new StdSchedulerFactory();

	    // 스케쥴러 등록 시도
	    try {

	    	// 스케쥴러 인스턴스 생성
	    	org.quartz.Scheduler scheduler = schedulerFactory.getScheduler();

	        JobDetail job = newJob(jobClass) // 실행할 클래스 설정
	            .withIdentity("jobName", org.quartz.Scheduler.DEFAULT_GROUP)
	            .build();

	        Trigger trigger = newTrigger()
	            .withIdentity("trggerName", org.quartz.Scheduler.DEFAULT_GROUP)
	            .withSchedule(cronSchedule(cron)) // cron으로 실행주기 설정
	            .build();

	        scheduler.scheduleJob(job, trigger);
	        scheduler.start();
	    }
	    catch(Exception e) {
	    	log.info("에러");
	    	e.printStackTrace();
    	}

	}

}
