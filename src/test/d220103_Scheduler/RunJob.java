package test.d220103_Scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


@Slf4j
public class RunJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("자바");
	}


}
