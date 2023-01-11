package init;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;
import translate.TranslationService;
import ui.UIMain;

import static utils.CommonUtils.waitForEnter;

// 잔디체커가 실행되는 내내 환경설정을 담고 있게 되는 클래스.
// 잔디체커가 시작되면 Jackson을 이용해 YAML 파일을 읽어오게 되고, 그 내용이 이 클래스의 각 변수에 채워진다.
@Slf4j
public class Initializer {

	// 환경변수 (내부 properties.yaml)
	public static String VERSION;
	public static String BUILD;
	public static final String PATH = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로

	// 환경변수 (외부 settings.yaml)
	private static String token_discordBot; // 토큰 (디코봇)
	private static String cron; // 스케쥴러 실행 주기
	private static String[][] members; // 참여인 목록
	private static String targetChannelId; // CRON 스케쥴러 실행 결과 메세지가 전송될 타겟 채널 ID
	private static String token_chatGPTAPI; // 토큰 (ChatGPT API)

	// 소개말
	public static String INFO_STRING;

	public static void ready(boolean needSwingWindow) throws Exception {

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

		// 2. YAML로 된 내부 환경변수 파일을 로드
		log.info("[환경설정 로드 2] 내부 환경변수 로드");
		loadProperties_inner();
		UIMain.getInstance().setTitle("잔디체커 " + Initializer.VERSION + " Build " + Initializer.BUILD);
		log.info("완료.");
		log.info("");

		// 3. YAML로 된 외부 환경변수 파일을 로드 (settings.yaml)
		log.info("[환경설정 로드 3] 외부 환경변수 로드");
		loadProperties_outer();
		log.info("완료.");
		log.info("");

		// 4. Load Google Translate API (googleAPIKey.json)
		log.info("[환경설정 로드 4] 구글 번역 API 로드");
		loadProperties_googleTranslationAPI();
		log.info("완료.");
		log.info("");

		log.info("[환경설정 로드] 완료!");
		log.info("");

	}

	// False option to not load Swing window logger
	public static void ready() throws Exception {
		ready(false);
	}


	// Getters/Setters
	public static String getToken_discordBot() { return token_discordBot; }
	public static String getCron() { return cron; }
	public static String getChId() { return targetChannelId; }
	public static String[][] getMembers() { return members ; }
	public static String getToken_chatGPTAPI() { return token_chatGPTAPI; }



	// Methods

	private static void loadProperties_inner() throws Exception {

		// load file contents to props
		Properties props;
		try(InputStream is = Initializer.class.getClassLoader().getResourceAsStream("properties.yaml")) {
			props = new Properties();
			props.load(is);
		} catch(Exception e) {
			log.error("properties.yaml 파일을 불러오는 중 오류가 발생했습니다.");
			throw new Exception(e);
		}

		// load props
		VERSION = props.getProperty("VERSION", "??????");
		BUILD = props.getProperty("BUILD", "?????");

		// show loaded props
		log.info("버전: {}", VERSION);
		log.info("빌드: {}", BUILD);

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
			""".formatted(VERSION, BUILD);

	}

	private static void loadProperties_outer() throws Exception {

		// 경로 확인
		log.info("외부 셋팅파일의 절대위치: {}", PATH);

		// 파일 객체 부르기
		File settingsFile = new File(PATH, "settings.yaml");
		if(!settingsFile.exists()) {
			log.error("환경설정 파일을 찾지 못했습니다. 실행파일과 같은 폴더에 settings.yaml 파일을 넣어주세요.");
			log.error("settings.yaml 파일의 작성법은 리포의 settings_form.yaml을 참고해 주시기 바랍니다.");
			waitForEnter("엔터 키를 누르시면 종료됩니다..");
			throw new Exception();
		}
		FileInputStream file = new FileInputStream(settingsFile);

		// ObjectMapper 생성자가, YAML파일을 오브젝트로 읽어들인다.
		// 그 다음 그 오브젝트를 MainSettings 클래스 각 변수에 맵핑시키는 식으로 그 내용을 읽어들인다. 자동이다!
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		InitializerSettingsFileVO vo = om.readValue(file, InitializerSettingsFileVO.class);
		file.close();
		log.info("MainSettings DTO 읽기 완료.");

		// 읽어온 내용을 MainSettings 클래스의 static 값들에 집어넣는다.
		token_discordBot = vo.getTokenJDA();
		cron  = vo.getCron();
		members = vo.getMembers();
		targetChannelId = vo.getTargetChannelId();
		token_chatGPTAPI = vo.getTokenChatGPTAPI();

		// 로드된 환경변수들 일괄 출력
		log.info("경로: {}", PATH);
		log.info("토큰(디스코드 JDA): {}", token_discordBot);
		log.info("크론: {}", cron);
		log.info("채널: {}", targetChannelId);
		log.info("명단 확인: ");
		for(int i = 0; i < members.length; i++) {
			String[] info = members[i];
			String name = info[0], gitHubId = info[1];
			String discordID = info.length >= 3 ? info[2] : null;
			String stickTextLeft = (i == members.length - 1) ? "└" : "├";
			log.info("    {}─ {}번째 인원: '{}' (Github ID: {}, Discord ID: {})", //디코 아이디가 안불러와짐..
					stickTextLeft, i, name, gitHubId, discordID);
		}
		log.info("토큰(ChatGPT API): {}", token_chatGPTAPI);

	}

	private static void loadProperties_googleTranslationAPI() throws Exception {
		TranslationService.init();
	}

}
