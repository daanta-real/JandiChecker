package init;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import translate.TranslationService;
import ui.UIMain;
import utils.CommonUtils;

// 잔디체커가 실행되는 내내 환경설정을 담고 있게 되는 클래스.
// 잔디체커가 시작되면 Jackson을 이용해 YAML 파일을 읽어오게 되고, 그 내용이 이 클래스의 각 변수에 채워진다.
@Slf4j
public class Initializer {

	// 1. Fields
	public static final String PATH = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로
	public static HashMap<String, String> VERSION;
	public static Map<String, String> props = new HashMap<>();
	public static HashMap<String, String> LANGUAGE;
	public static Map<String, Map<String, String>> MEMBERS = new HashMap<>();
	public static String INFO_STRING;

	// 2. Methods
	private static String getStringFromAddr(XSSFSheet sheet, int rowIndex, int colIndex) {
		Row row = CellUtil.getRow(rowIndex, sheet);
		return CellUtil.getCell(row, colIndex).getStringCellValue();
	}

	private static void getPropsInfo(XSSFSheet sheet) {
		props.put("version", getStringFromAddr(sheet, 2, 6));
		props.put("build", getStringFromAddr(sheet, 3, 6));
		props.put("language", getStringFromAddr(sheet, 6, 6));
		props.put("cronSchedule", getStringFromAddr(sheet, 9, 6));
		props.put("cronTargetChannelID", getStringFromAddr(sheet, 10, 6));
		props.put("JDAToken", getStringFromAddr(sheet, 13, 6));
		props.put("ChatGPTToken", getStringFromAddr(sheet, 14, 6));
		StringBuilder sb = new StringBuilder();
		for(int i = 15; i <= 26; i++) {
			sb.append(getStringFromAddr(sheet, i, 6));
			if(i != 26) sb.append("\n");
		}
		props.put("GoogleCloudToken", sb.toString());
		log.debug("FINISHED PROPS LOADING! {}", props);
	}

	private static void getMembersInfo(XSSFSheet sheet) {

		// Result
		Map<String, Map<String, String>> membersInfo = new HashMap<>();

		int count = 3; // start from 4th row
		while(true) {

			Row row = CellUtil.getRow(count, sheet);

			Cell cellName = CellUtil.getCell(row, 1);
			if(cellName.getCellType() == CellType.BLANK) break; // Name is required, and no more members info from this row
			String name = cellName.getStringCellValue();

			Map<String, String> memberParam = new HashMap<>();
			memberParam.put("name", name);

			Cell cellGitHubID = CellUtil.getCell(row, 2);
			if(cellGitHubID.getCellType() == CellType.BLANK) continue; // GitHubID is required
			String gitHubID = cellGitHubID.getStringCellValue();
			memberParam.put("gitHubID", gitHubID);

			Cell cellDiscordTagID = CellUtil.getCell(row, 3);
			if(cellDiscordTagID.getCellType() != CellType.BLANK) {
				String discordTagID = cellDiscordTagID.getStringCellValue(); // Discord ID is optional
				memberParam.put("discordTagID", discordTagID);
			}

			log.debug("MEMBERS '{}' FOUND: {}", name, memberParam);
			membersInfo.put(name, memberParam);
			count++;

		}

		MEMBERS = membersInfo;
		log.debug("FINISHED MEMBER LOADING: {}", MEMBERS);

	}

	private static void loadXLSXSettings() throws Exception {
		try(
				// 파일 객체 부르기
				FileInputStream file = new FileInputStream(new File(PATH, "settings.xlsx"));
				// 파일 객체로부터 시트 객체 뽑아내기
				XSSFWorkbook workbook = new XSSFWorkbook(file)
				// try가 다 끝나면 위의 file과 workbook 객체에 대해 .close()가 실행되며 파일이 닫아진다.
				// sheet 필드는 이 try 구문이 끝나면 소거되어 버리니, 처리할 것이 있으면 이 안에서 할 것.
		) {
			// 최종적으로 sheet 객체를 얻는다.
			XSSFSheet sheet = workbook.getSheetAt(0);
			log.debug("sheet loading success: {}", sheet);
			getMembersInfo(sheet);
			getPropsInfo(sheet);
		} catch(Exception e) {
			throw new Exception(e);
		}
	}

	public static void ready(boolean needSwingWindow) throws Exception {

		// Start
		log.info("");
		log.info("<<< INITIALIZER START >>>");
		log.info("");

		// 1. Load user settings (settings.yaml)
		log.info("<<< CONFIGURATION 1. LOADING USER DEFINED VALUES >>>");
		loadXLSXSettings();
		log.info("FINISHED.");
		log.info("");

		// 2. Load language settings ((your language).yaml)
		log.info("<<< CONFIGURATION 2. LOADING LANGUAGE FILE >>>");
		String language = Initializer.props.get("language");
		LANGUAGE = CommonUtils.loadYaml("languages/" + language + ".yaml"); // Language props loaded
		log.info(LANGUAGE.get("fin"));
		log.info("");

		// 3. Load version & build settings (version.yaml)
		log.info(LANGUAGE.get("initializer_3_loadVersion"));
		VERSION = CommonUtils.loadYaml("version.yaml");
		log.info(LANGUAGE.get("fin"));
		log.info("");

		// 4. 윈도 컨트롤, 트레이 등 ui 준비
		if(needSwingWindow) {
			log.info(LANGUAGE.get("initializer_4_loadSwingWindow"));
			UIMain.getInstance().init();
		} else {
			log.info(LANGUAGE.get("initializer_4_notLoadSwingWindow"));
		}
		log.info("{}", LANGUAGE.get("fin"));
		log.info("");

		// 5. Load Google Translate API (googleAPIKey.json)
		log.info(LANGUAGE.get("initializer_5_loadGoogleTranslate"));
		TranslationService.init();
		log.info(LANGUAGE.get("fin"));
		log.info("");

		// 4. Load app info message
		// Set info String
		INFO_STRING = """
			```md
			%s```
			""".formatted(LANGUAGE.get("appInfo"))
				.formatted(VERSION.get("version"), VERSION.get("build"));
		// Finished
		log.info(LANGUAGE.get("initializer_6_finishedLoading"));
		log.info("");

	}

	// False option to not load Swing window logger
	public static void ready() throws Exception {
		ready(false);
	}

	// 그룹원명을 입력하면 깃헙 ID를 리턴
	public static String getGitHubIDByMemberName(String memberName) throws Exception {
		if(!MEMBERS.containsKey(memberName)) throw new Exception();
		Map<String, String> member = MEMBERS.get(memberName);
		return member.get("gitHubID");
	}

	// 디스코드 ID를 입력하면 멤버명을 리턴
	public static String getMemberNameByDiscordTagID(String discordTagID) throws Exception {
		for(Map.Entry<String, Map<String, String>> entry: MEMBERS.entrySet()) {
			Map<String, String> memberProps = entry.getValue();
			if(!memberProps.containsKey("discordTagID")) continue;
			String foundDiscordTagID = memberProps.get("discordTagID");
			if(!StringUtils.isEmpty(foundDiscordTagID) && foundDiscordTagID.equals(discordTagID)) {
				return memberProps.get("name");
			}
		}
		throw new Exception();
	}

	// 디스코드 ID로 그룹원의 이름과 GitHub ID를 Map으로 리턴
	public static Map<String, String> getMemberInfoesByDiscordTagID(String discordTagID) throws Exception {
		for(Map.Entry<String, Map<String, String>> entry: MEMBERS.entrySet()) {
			Map<String, String> memberProp = entry.getValue();
			if(!memberProp.containsKey("discordTagID")) continue; // TODO test
			String foundDiscordTagID = memberProp.get("discordTagID");
			if(!StringUtils.isEmpty(foundDiscordTagID) && foundDiscordTagID.equals(discordTagID)) return memberProp;
		}
		throw new Exception();
	}

}
