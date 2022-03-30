
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import $.$;
import jda.Jda;
import scheduler.CronJob;
import scheduler.CronScheduler;
import settings.MainSettings;

public class Main {

	// 출력 도구 생성
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(Main.class);

	// 서버의 실행
	public static void main(String[] args) throws Exception {
/*
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
		}*/

/* windows */
		// windows only
		/*
		Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe");
		p.waitFor();
		*/

		/* mac */
		//Runtime.getRuntime().exec("/usr/bin/open -a Terminal \"" + MainSettings.PATH + "\"");
		/*
		boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
		if(isMac) {
			String path = MainSettings.PATH;
			String command = "tell application \"Terminal\"\n"
				+ "do script \"java -jar \'" + path + "\' \JandiChecker.
		}*/

		/*
	    if(args.length == 0 && System.getProperty("os.name").toLowerCase().contains("mac")){
	        try {
	            String path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
	            String command = "tell application \"Terminal\"\n" +
	                    "do script \"java -jar \'" + path + "\' isInConsole\"\n" +
	                    "close the front window\n" + // because "do script..." opens another window
	                    "activate\n" +
	                    "end tell";
	            String[] arguments = new String[]{"osascript", "-e", command};

	            Runtime.getRuntime().exec(arguments);
	            System.exit(0);
	        } catch (IOException | URISyntaxException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
		$.pn(MainSettings.PATH);
		Runtime.getRuntime().exec("/usr/bin/open -a Terminal " + MainSettings.PATH);
		System.exit(0);*/

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

