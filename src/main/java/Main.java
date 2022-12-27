
import utils.jda.JdaController;

import lombok.extern.slf4j.Slf4j;

import utils.logging.JTextAppender;
import utils.scheduler.CronScheduler;

import init.Initializer;

import static utils.CommonUtils.waitForEnter;

@Slf4j
public class Main {

	static JTextAppender appender = new JTextAppender();

	// 실제 실행
	public static void main(String[] args) {

		appender.init();

		try {

			// 환경설정 로드
			log.info("");
			log.info("");
			log.info("");
			log.info("[[[잔디체커 환경설정 로드]]]");
			Initializer.ready();

			// 타이틀 표시
			log.info("""


					**********************************************
					- JandiChecker {} Build {}
					- Github 잔디 점검 프로그램
					**********************************************
					""", Initializer.VERSION, Initializer.BUILD);

			// JDA 로드
			log.info("");
			log.info("[[[잔디체커 JDA 로드]]]");
			JdaController.ready();

			// 스케쥴러 실행
			log.info("");
			log.info("[[[스케쥴러 시작]]]");
			CronScheduler.run();

			// 실행 완료
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
