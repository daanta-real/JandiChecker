package init;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;
import UI.UIMain;

import static utils.CommonUtils.waitForEnter;

// 잔디체커가 실행되는 내내 환경설정을 담고 있게 되는 클래스.
// 잔디체커가 시작되면 Jackson을 이용해 YAML 파일을 읽어오게 되고, 그 내용이 이 클래스의 각 변수에 채워진다.
@Slf4j
public class Initializer {

	// 환경변수 (내부 properties.yaml)
	public static String VERSION;
	public static String BUILD;
	public static final String PATH = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로
	private static String CMD_CHAR; // 잔디체커 명령임을 판독하는 기준이 되는 구분자

	// 환경변수 (외부 settings.yaml)
	private static String token; // 토큰
	private static String cron; // 스케쥴러 실행 주기
	private static String[][] members; // 참여인 목록
	private static String targetChannelId; // CRON 스케쥴러 실행 결과 메세지가 전송될 타겟 채널 ID

	// 소개말
	public static String INFO_STRING = """
			```md
			_**저는 매일 자정, 잔디를 심는 데 성공한 사람들을 찾아낼 것입니다.**_
			&목표: 이 봇이 제작된 목표를 설명합니다.
			&정보 [사람이름]: 특정인의 최근 1년 간 및 근 30일 간의 Github 잔디 정보를 가져옵니다. 이때 관리목록에 이름이 서로 겹치는 인원이 없다면 성은 생략해도 됩니다.
			&id [id]: 특정 id의 최근 1년 간 및 근 30일 간의 Github 잔디 정보를 가져옵니다. 관리목록에 없는 사람도 조회 가능합니다.
			&어제: 어제 잔디를 심은 사람들의 명단을 공개합니다.
			&어제안함: 어제 잔디를 심지 않은 사람들의 명단을 공개합니다.
			&오늘안함: 오늘 잔디를 심지 않은 사람들의 명단을 공개합니다.
			&확인 [날짜(yyyy-MM-dd 형식)]: 특정 날짜에 잔디를 제출하지 않은 사람들의 명단을 출력합니다.
					
			잔디체커(JandiChecker) %s Build %s
			제작 by 단타(박준성)
			e-mail: daanta@naver.com
			Github: http://github.com/daanta-real
			```
			""".formatted(VERSION, BUILD);

	public static void ready() throws Exception {

		log.info("환경설정 로드 시작..");

		// 1. 윈도 컨트롤, 트레이 등 UI 준비
		UIMain.getInstance().init();
		log.info("윈도 컨트롤 준비 완료.");

		// 2. YAML로 된 내부 환경변수 파일을 로드
		loadProperties_inner();
		log.info("내부 환경변수 로드 완료.");

		// 3. YAML로 된 외부 환경변수 파일을 로드 (settings.yaml)
		loadProperties_outer();
		log.info("외부 환경변수 로드 완료.");

		log.info("환경설정 로드 끝.");

	}

	// Getters/Setters
	public static String     getToken  () { return token   ; }
	public static String     getCron   () { return cron    ; }
	public static String     getChId   () { return targetChannelId; }
	public static String[][] getMembers() { return members ; }
	public static String     getCmdChar() { return CMD_CHAR; }

	// Methods

	private static void loadProperties_inner() throws Exception {

		// find file
		URL resource = Initializer.class.getClassLoader().getResource("properties.yaml");
		URI location = resource.toURI();
		log.info("내부 환경변수 파일의 위치: {}", location);

		File configFile = new File(location);
		if(!configFile.exists()) {
			log.error("properties.yaml 파일이 없는 것 같습니다.");
			throw new Exception();
		}

		// load file contents to props
		Properties props;
		try(InputStream is = new FileInputStream(configFile)) {
			props = new Properties();
			props.load(is);
		} catch(Exception e) {
			log.error("properties.yaml 파일 내부를 읽는 중 에러가 발생했습니다.");
			throw new Exception(e);
		}

		VERSION = props.getProperty("VERSION", "??????");
		BUILD = props.getProperty("BUILD", "?????");
		CMD_CHAR = props.getProperty("CMD_CHAR", "???");

		log.info("버전: {}", VERSION);
		log.info("빌드: {}", BUILD);
		log.info("명령어: {}", CMD_CHAR);

	}

	private static void loadProperties_outer() throws Exception {

		// 경로 확인
		log.info("외부 셋팅파일을 찾는 기준 위치: {}", PATH);

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
		InitializerSettingsFileVO dto = om.readValue(file, InitializerSettingsFileVO.class);
		file.close();
		log.info("MainSettings DTO 읽기 완료.");

		// 읽어온 내용을 MainSettings 클래스의 static 값들에 집어넣는다.
		token = dto.getToken();
		cron  = dto.getCron();
		members = dto.getMembers();
		targetChannelId = dto.getTargetChannelId();

		// 로드된 환경변수들 일괄 출력
		log.info("경로: {}", PATH);
		log.info("토큰: {}", token);
		log.info("크론: {}", cron);
		log.info("채널: {}", targetChannelId);
		log.info("명단 확인: ");
		for(int i = 0; i < members.length; i++)
			log.info("    {}─ {}번째 인원: '{}' (Github ID: {})",
					(i == members.length - 1 ? '└' : '├'), i, members[i][0], members[i][1]);

	}

}
