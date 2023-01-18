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

// 잔디체커가 실행되는 내내 환경설정을 담고 있게 되는 클래스.
// 잔디체커가 시작되면 Jackson을 이용해 YAML 파일을 읽어오게 되고, 그 내용이 이 클래스의 각 변수에 채워진다.
@Slf4j
public class Initializer {

	// 1. Fields
	public static final String PATH = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로
	public static Map<String, String> props = new HashMap<>();
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

	private static void loadProperties() throws Exception {
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
		log.info("[환경설정 로드] 시작...");
		log.info("");

		// 1. 윈도 컨트롤, 트레이 등 ui 준비
		if(needSwingWindow) {
			log.info("[환경설정 로드 1] Swing 윈도 컨트롤 준비");
			UIMain.getInstance().init();
		} else {
			log.info("[환경설정 로드 1] Swing 윈도 컨트롤을 준비하지 않습니다.");
		}
		log.info("완료.");
		log.info("");

		// 2. 환경변수 로드
		log.info("[환경설정 로드 2] 환경변수 로드");
		loadProperties();
		log.info("완료.");
		log.info("");

		// 3. Load Google Translate API (googleAPIKey.json)
		log.info("[환경설정 로드 3] 구글 번역 API 로드");
		TranslationService.init();
		log.info("완료.");
		log.info("");

		// 4. Load app info message
		// Set info String
		INFO_STRING = """
			```md
			
			_**저는 매일 자정, 잔디를 심는 데 성공한 사람들을 찾아낼 것입니다.**_
			
			# 잔디체커의 사용법
			잔디체커는 세 가지 방법으로 이용할 수 있습니다.
			1. 채팅창에 '/'(슬래시)를 입력하면 사용 가능한 명령어들이 슬래시 메뉴로 표시됩니다. 원하는 것을 선택한 후, 추가적인 옵션 입력이 필요하면 옵션을 입력해 주세요.
			2. 채팅창에 '잔디야'라고 입력하면 사용 가능한 명령들이 버튼 메뉴로 표시됩니다. 원하는 것을 누른 후, 추가적인 옵션 입력이 필요하면 옵션을 입력해 주세요.
			3. '잔디야 [질문내용]'이라고 입력하면, 메뉴 호출 없이 AI에게 바로 질문할 수 있습니다.
			
			# 각 커맨드에 대한 상세 소개
			/나: 나의 잔디정보를 출력합니다. 정보를 얻기 위해서는 그룹원에 소속되어 있어야 합니다.
			/잔디야: 일반적인 질문에 답하는 ChatGPT AI에게 질문하여 답변을 얻습니다. 질문은 한국어로 입력하면 됩니다.
			/정보 [이름]: 특정 그룹원의 종합 잔디정보를 출력합니다. 성이름 모두 입력해도 되고 이름만 입력해도 됩니다.
			/id [ID]: 특정 Github ID의 종합 잔디정보를 출력합니다.
			/어제: 어제 잔디심기에 성공한 그룹원의 목록을 출력합니다.
			/어제안함: 어제 잔디심기를 패스한 사람의 목록을 출력합니다.
			/날짜 [yyyyMMdd]: 특정 날짜에 잔디를 심은 사람의 목록을 출력합니다. 현재 이전 날짜에 대해서 검색이 가능합니다.
			/대하여: 이 봇을 소개합니다.
			
			# 팁
			AI에게 질문하실 때에는 최대한 구체적이고 자세하게 질문해 주세요.
			질문을 정확하게 정의할수록 답변도 정확하게 돌아옵니다.

			# App information
			[잔디체커(JandiChecker)] (%s Build %s)
			* Stacks used: Java 17, Gradle, IntelliJ, JDA, Quartz, ChatGPT API, Google Cloud API(for translation), Swing, JUnit5, Logback
			* Have taking an effort to comply with TDD.

			# Developer's informaiton
			* Developed by 단타(박준성) / Daanta(Junsung Park)
			* GitHub: http://github.com/daanta-real
			* Blog  : http://blog.naver.com/daanta
			* e-mail: daanta@naver.com
			
			```
			""".formatted(props.get("version"), props.get("build"));

		// Finished
		log.info("[환경설정 로드] 완료!");
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
