
import lombok.extern.slf4j.Slf4j;
import jda.JDAController;

import logging.JTextAppender;
import scheduler.CronScheduler;

import init.Initializer;

import static init.Initializer.pr;
import static utils.CommonUtils.waitForEnter;

@Slf4j
public class Main {

	public static void main(String[] args) {

		try {

			// Apply custom logback
			JTextAppender.init();

			// Load all preferences
			log.info("<<< INITIALIZING JandiChecker >>>");
			Initializer.ready(true);

			// Show title
			log.info("""


					**********************************************
					- {} {} Build {}
					- {}
					**********************************************
					""", pr.l("appName"), pr.getVersion(), pr.getBuild(), pr.l("main_description"));

			// Load JDA
			log.info("");
			log.info("{}", pr.l("main_jdaLoad"));
			JDAController.init();

			// Run scheduler
			log.info("");
			log.info("{}", pr.l("main_startScheduler"));
			CronScheduler.run();

			// Done!
			log.info("");
			log.info("{}", pr.l("main_execute"));
			log.info("");

		} catch(Exception e) {
			waitForEnter();
			System.exit(-1);
			// Automatically return - exit
		}

	}

}
