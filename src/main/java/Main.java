
import utils.jda.JdaController;

import lombok.extern.slf4j.Slf4j;

import utils.scheduler.CronScheduler;

import init.Initializer;

import javax.swing.*;

import static utils.CommonUtils.waitForEnter;

@Slf4j
public class Main extends JFrame {

	// 실제 실행
	public static void main(String[] args) {

		try {

			// 타이틀 표시
			System.out.printf("""
					**********************************************
					- JandiChecker %s Build %s
					- Github 잔디 점검 프로그램
					**********************************************%n%n""", Initializer.VERSION, Initializer.BUILD);
			System.out.println();

			// 환경설정 로드
			log.info("[[[잔디체커 환경설정 로드]]]");
			Initializer.ready();

			// JDA 로드
			System.out.println();
			log.info("[[[잔디체커 JDA 로드]]]");
			JdaController.load();

			// 스케쥴러 실행
			System.out.println();
			log.info("[[[스케쥴러 시작]]]");
			CronScheduler.run();

			// 실행 완료
			System.out.println();
			log.info("[[[잔디체커 실행 완료]]]");
			System.out.println();

		} catch(Exception e) {
			waitForEnter();
			System.exit(-1);
			// 자동으로 return됨 = 강제종료
		}

	}

}
