package scheduler;

import static init.Pr.pr;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class CronScheduler {

	// The method which creates the new Scheduler instance
	public static void scheduleExecute(Class<? extends Job> jobClass, String cron) throws Exception {

		// Get the Scheduler instance from factory
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();

		// Create the Job and Trigger instance: the instance to execute and to trigger to apply
		JobDetail job = newJob(jobClass)
			.withIdentity("jobName", org.quartz.Scheduler.DEFAULT_GROUP)
			.build();
		Trigger trigger = newTrigger()
			.withIdentity("trggerName", org.quartz.Scheduler.DEFAULT_GROUP)
			.withSchedule(cronSchedule(cron))
			.build();

		// Apply the Job and Trigger instances to the Scheduler instance
		scheduler.scheduleJob(job, trigger);

		// Start the scheduler.
		// Now executing CronScheduler.run() outside runs this and scheduler actually starts
		scheduler.start();

	}

	// Execute the scheduler
	public static void run() throws Exception {
		scheduleExecute(CronJob.class, pr.getCronSchedule());
	}

}
