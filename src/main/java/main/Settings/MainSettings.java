package main.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import $.$;

// 잔디체커가 실행되는 내내 환경설정을 담고 있게 되는 클래스.
// 잔디체커가 시작되면 Jackson을 이용해 YAML 파일을 읽어오게 되고, 그 내용이 이 클래스의 각 변수에 채워진다.
public class MainSettings {

	// 환경상수
	private static final String path = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로

	// 환경변수
	private static boolean debug; // 디버그 여부
	private static String token; // 토큰
	private static String cron; // 스케쥴러 실행 주기
	private static String[][] members; // 참여인 목록

	// 소개말
	public static String INFO_STRING = "```md\n_**저는 매일 저녁과 자정, 잔디를 심지 않은 사람들을 찾아내 그 명단을 발표할 것입니다.**_\n"
		+ "&목표: 이 봇이 제작된 목표를 설명합니다.\n"
		+ "&정보 [사람이름]: 특정인의 최근 1년 간 및 근 30일 간의 Github 잔디 정보를 가져옵니다. 이때 관리목록에 이름이 서로 겹치는 인원이 없을 경우, 성은 생략해도 무관합니다.\n"
		+ "&id [id]: 특정 id의 최근 1년 간 및 근 30일 간의 Github 잔디 정보를 가져옵니다.\n"
		+ "&어제: 어제 잔디를 심은 사람들의 명단을 공개합니다.\n"
		+ "&어제안함: 어제 잔디를 심지 않은 사람들의 명단을 공개합니다.\n"
		+ "&오늘안함: 오늘 잔디를 심지 않은 사람들의 명단을 공개합니다.\n"
		+ "&확인 [날짜(yyyy-MM-dd 형식)]: 특정 날짜에 잔디를 제출하지 않은 사람들의 명단을 출력합니다.\n"
		+ "\n"
		+ "잔디체커(JandiChecker) v1.0\n제작 by 단타(박준성)\ne-mail: daanta@naver.com\nGithub: http://github.com/daanta-real```";

	// YAML로 된 환경변수 파일을 로드
	public static void ready() throws Exception {

		$.pf(" - 환경설정 로드 시작..");

		// 파일 객체 부르기
		FileInputStream file = new FileInputStream(new File(MainSettings.path, "settings.yaml"));

		// ObjectMapper 생성자가, YAML파일을 오브젝트로 읽어들인다.
		// 그 다음 그 오브젝트를 MainSettings 클래스 각 변수에 맵핑시키는 식으로 그 내용을 읽어들인다. 자동이다!
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		MainSettingsVO dto = om.readValue(file, MainSettingsVO.class);
		file.close();
		$.pn(" MainSettings DTO 읽기 완료.");

		// 읽어온 내용을 MainSettings 클래스의 static 값들에 집어넣는다.
		debug = dto.isDebug();
		token = dto.getToken();
		cron  = dto.getCron();
		members = dto.getMembers();

		// 로드된 환경변수들 일괄 출력
		$.pf(" - 디버그 모드: %s\n", debug);
		$.pf(" - 경로: %s\n", path);
		$.pf(" - 토큰: %s\n", token);
		$.pf(" - 크론: %s\n", cron);
		$.pn(" - 명단 확인: ");
		for(int i = 0; i < members.length; i++)
			$.pf("    %c─ %d번째 인원: '%s' (Github ID: %s)\n",
				(i == members.length - 1 ? '└' : '├'), i, members[i][0], members[i][1]);

		$.pn(" - 환경설정 로드 끝.");

	}

	// Getters/Setters
	public static boolean    isDebug   () { return debug  ; }
	public static String     getToken  () { return token  ; }
	public static String     getCron   () { return cron   ; }
	public static String     getPath   () { return path   ; }
	public static String[][] getMembers() { return members; }
	public static void setDebug  (boolean    debug  ) { MainSettings.debug   = debug  ; }
	public static void setToken  (String     token  ) { MainSettings.token   = token  ; }
	public static void setCron   (String     cron   ) { MainSettings.cron    = cron   ; }
	public static void setMembers(String[][] members) { MainSettings.members = members; }

}
