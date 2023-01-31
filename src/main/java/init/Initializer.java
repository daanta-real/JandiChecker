package init;

import lombok.extern.slf4j.Slf4j;
import translate.TranslationService;
import ui.UIMain;

// 잔디체커가 실행되는 내내 환경설정을 담고 있게 되는 클래스.
// 잔디체커가 시작되면 Jackson을 이용해 YAML 파일을 읽어오게 되고, 그 내용이 이 클래스의 각 변수에 채워진다.
@Slf4j
public class Initializer {

	// 1. Fields
	public static Props props = new Props();

	// 2. Methods

	public static void ready(boolean needSwingWindow) throws Exception {

		// Start
		log.info("");
		log.info("<<< INITIALIZER START >>>");
		log.info("");

		// 1. Load user settings (settings.yaml)
		log.info("<<< CONFIGURATION 1. LOADING USER DEFINED VALUES >>>");
		LoaderXLSX.loadXLSXSettings();
		log.info("FINISHED.");
		log.info("");

		// 2. Load language settings ((your language).yaml)
		log.info("<<< CONFIGURATION 2. LOADING LANGUAGE FILE >>>");
		props.loadLanguageInfoes();
		log.info(props.lang("fin"));
		log.info("");

		// 3. Load version & build settings (version.yaml)
		log.info(props.lang("initializer_3_loadVersion"));
		props.loadVersionInfoes();
		log.info(props.lang("fin"));
		log.info("");

		// 4. 윈도 컨트롤, 트레이 등 ui 준비
		if(needSwingWindow) {
			log.info(props.lang("initializer_4_loadSwingWindow"));
			UIMain.getInstance().init();
		} else {
			log.info(props.lang("initializer_4_notLoadSwingWindow"));
		}
		log.info("{}", props.lang("fin"));
		log.info("");

		// 5. Load Google Translate API (googleAPIKey.json)
		log.info(props.lang("initializer_5_loadGoogleTranslate"));
		TranslationService.init();
		log.info(props.lang("fin"));
		log.info("");

		// 4. Load app info message
		// Set info String
		props.setInformation("""
			```md
			%s```
			""".formatted(props.lang("appInfo"))
				.formatted(props.getVersion(), props.getBuild()));
		// Finished
		log.info(props.lang("initializer_6_finishedLoading"));
		log.info("");

	}

	// False option to not load Swing window logger
	public static void ready() throws Exception {
		ready(false);
	}

}
