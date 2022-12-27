
import lombok.extern.slf4j.Slf4j;
import utils.jda.JdaController;

import utils.logging.JTextAppender;
import utils.scheduler.CronScheduler;

import init.Initializer;

import static utils.CommonUtils.waitForEnter;

@Slf4j
public class Main {

	public static void main(String[] args) {

		try {

			// Apply custom logback
			JTextAppender.init();

			// Load all preferences
			log.info("[[[잔디체커 환경설정 로드]]]");
			Initializer.ready();

			// Show title
			log.info("""


					**********************************************
					- JandiChecker {} Build {}
					- Github 잔디 점검 프로그램
					**********************************************
					""", Initializer.VERSION, Initializer.BUILD);

			// Load JDA
			log.info("");
			log.info("[[[잔디체커 JDA 로드]]]");
			JdaController.ready();

			// Run scheduler
			log.info("");
			log.info("[[[스케쥴러 시작]]]");
			CronScheduler.run();

			// Done!
			log.info("");
			log.info("[[[잔디체커 실행 완료]]]");
			log.info("");

		} catch(Exception e) {
			waitForEnter();
			System.exit(-1);
			// 자동으로 return됨 = 강제종료
		}

	}

}
