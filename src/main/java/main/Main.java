package main;

import $.$;
import main.data.MainSystem;
import main.libraries.Jda;
import main.libraries.Scheduler.CronJob;
import main.libraries.Scheduler.CronScheduler;

class Main {

	// 로딩모듈
	public static void ready() throws Exception {

		// 환경설정 로드
		$.pn("\n[[[잔디체커 환경설정 로드]]]\n");

		// JDA 로드
		$.pn("\n[[[잔디체커 JDA 로드]]]\n");
		Jda.load();

		// 스케쥴러 로드
		$.pn("\n[[[스케쥴러 로드]]]\n");
		CronScheduler.run(CronJob.class, MainSystem.CRON);

	}

	// 서버의 실행
	public static void main(String[] args) throws Exception {

		$.pn("[[[잔디체커 시작]]]");
		ready();
		$.pn("\n[[[잔디체커 실행 완료]]]\n");

	}

}
