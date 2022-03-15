package main;

import $.$;
import main.Scheduler.CronJob;
import main.Scheduler.CronScheduler;
import main.Settings.MainSettings;
import main.libraries.Jda;

class Main {

	// 로딩모듈
	public static void ready() throws Exception {

		// 환경설정 로드
		$.pn("\n[[[잔디체커 환경설정 로드]]]\n");
		MainSettings.ready();

		// JDA 로드
		$.pn("\n[[[잔디체커 JDA 로드]]]\n");
		Jda.load();

		// 스케쥴러 로드
		$.pn("\n[[[스케쥴러 시작]]]\n");
		CronScheduler.run(CronJob.class, MainSettings.getCron());

	}

	// 서버의 실행
	public static void main(String[] args) throws Exception {

		$.pn("[[[잔디체커 시작]]]");
		ready();
		$.pn("\n[[[잔디체커 실행 완료]]]\n");
// 메세지가 들어올 때마다 디버그 콘솔이 출력되는 문제가 있음.

	}

}
