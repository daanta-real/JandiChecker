
import lombok.extern.slf4j.Slf4j;
import jda.JDAController;

import logging.JTextAppender;
import scheduler.CronScheduler;

import init.Initializer;

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
					- JandiChecker {} Build {}
					- {}
					**********************************************
					""", Initializer.VERSION.get("version"), Initializer.VERSION.get("build"), Initializer.LANGUAGE.get("main_description"));

			// Load JDA
			log.info("");
			log.info("{}", Initializer.LANGUAGE.get("main_jdaLoad"));
			JDAController.init();

			// Run scheduler
			log.info("");
			log.info("{}", Initializer.LANGUAGE.get("main_startScheduler"));
			CronScheduler.run();

			// Done!
			log.info("");
			log.info("{}", Initializer.LANGUAGE.get("main_execute"));
			log.info("");

		} catch(Exception e) {
			waitForEnter();
			System.exit(-1);
			// 자동으로 return됨 = 강제종료
		}

	}

}
