package init;

import lombok.extern.slf4j.Slf4j;
import translate.TranslationService;
import ui.UIMain;

import static init.Pr.pr;

// This is used to loading JandiChecker's all the properties like words, members and UI, etc.
@Slf4j
public class Initializer {


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
		pr.loadLanguageInfoes();
		log.info(pr.l("fin"));
		log.info("");

		// 3. Load version & build settings (version.yaml)
		log.info("<<< CONFIGURATION 3. LOAD VERSION INFO >>>");
		pr.loadVersionInfoes();
		log.info(pr.l("fin"));
		log.info("");

		// 4. Load all UIs including Swing window instance, icon, tray, etc.
		if(needSwingWindow) {
			log.info("<<< CONFIGURATION 4. LOADING SWING WINDOW CTRL >>>");
			UIMain.init();
		} else {
			log.info("<<< CONFIGURATION 4. 'NOT' LOADING SWING WINDOW CTRL >>>");
		}
		log.info("{}", pr.l("fin"));
		log.info("");

		// 5. Load Google Translate API (googleAPIKey.json)
		log.info("<<< CONFIGURATION 5. LOADING GOOGLE TRANSLATE API >>>");
		TranslationService.init();
		log.info(pr.l("fin"));
		log.info("");

		// 4. Load app info message
		// Set info String
		pr.setInformation("""
			```md
			%s```
			""".formatted(pr.l("appInfo"))
				.formatted(pr.getVersion(), pr.getBuild()));
		// Finished
		log.info("<<< CONFIGURATION 6. CONFIGURATION FINISHED >>>");
		log.info(pr.l("initializer_6_finishedLoading"));
		log.info("");

	}

	// The false executing option to not load Swing window logger
	public static void ready() throws Exception {
		ready(false);
	}

}
