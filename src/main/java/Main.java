
import java.awt.GraphicsEnvironment;
import java.io.Console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import $.$;
import jda.Jda;
import scheduler.CronJob;
import scheduler.CronScheduler;
import vo.MainSettings;

public class Main {

	// 출력 도구 생성
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(Main.class);

	// 서버의 실행
	public static void main(String[] args) throws Exception {

		// 콘솔을 강제로 열어주는 코드. 윈도에서는 무조건 콘솔이 열리나 맥에서는 그렇지 않다.
		// 맥을 위해 필요한 코드이다.
		Console console = System.console();
		if (console == null && !GraphicsEnvironment.isHeadless()) {
			String filename = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
			Runtime.getRuntime()
				.exec(new String[] { "cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\"" });
		} else {
			Main.main(new String[0]);
			System.out.println("Program has ended, please type 'exit' to close the console");
		}

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
