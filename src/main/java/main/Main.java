package main;

import $.$;
import main.Jda.Jda;
import main.Scheduler.CronJob;
import main.Scheduler.CronScheduler;
import main.Settings.MainSettings;

class Main {

	// 서버의 실행
	public static void main(String[] args) throws Exception {

		// 환경설정 로드
		$.pn("\n[[[잔디체커 환경설정 로드]]]\n");
		MainSettings.ready();

		// JDA 로드
		$.pn("\n[[[잔디체커 JDA 로드]]]\n");
		Jda.load();

		// 스케쥴러 실행
		$.pn("\n[[[스케쥴러 시작]]]\n");
		CronScheduler.run(CronJob.class, MainSettings.getCron());

		$.pn("\n[[[잔디체커 실행 완료]]]\n");

	}

}
