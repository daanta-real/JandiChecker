
import jda.JdaController;

import lombok.extern.slf4j.Slf4j;

import scheduler.CronJob;
import scheduler.CronScheduler;

import configurations.Configurations;

import javax.swing.*;

import static utils.Utils.waitForEnter;

@Slf4j
public class Main extends JFrame {

	// Fields
	public static boolean windowVisible = true;

	// Minimalization
	public void goTray() {
		System.out.println("트레이로");
	}

	// Constructor
	private Main() {
		super("잔디체커 " + Configurations.VERSION + " Build " + Configurations.BUILD);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 실제 실행
	public static void main(String[] args) {

		try {
/*
			// MacOS only
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
					e.printStackTrace();
				}
			}
			log.info(MainSettings.PATH);
			Runtime.getRuntime().exec("/usr/bin/open -a Terminal " + MainSettings.PATH);
			System.exit(0);*/

			// 환경설정 로드
			System.out.printf("""
					**********************************************
					- JandiChecker %s Build %s
					- Github 잔디 점검 프로그램
					**********************************************%n%n""", Configurations.VERSION, Configurations.BUILD);
			System.out.println();
			log.info("[[[잔디체커 환경설정 로드]]]");
			Configurations.ready();

			// JDA 로드
			System.out.println();
			log.info("[[[잔디체커 JDA 로드]]]");
			JdaController.load();

			// 스케쥴러 실행
			System.out.println();
			log.info("[[[스케쥴러 시작]]]");
			CronScheduler.run(CronJob.class, Configurations.getCron());

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
