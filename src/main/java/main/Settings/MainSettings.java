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

	// YAML로 된 환경변수 파일을 로드
	public static void ready() throws Exception {

		$.pf(" - 설정파일 읽기 시작..");
		// 파일 객체 부르기
		FileInputStream file = new FileInputStream(new File(MainSettings.path, "settings.yaml"));

		// ObjectMapper 생성자가, YAML파일을 오브젝트로 읽어들인다.
		// 그 다음 그 오브젝트를 MainSettings 클래스 각 변수에 맵핑시키는 식으로 그 내용을 읽어들인다. 자동이다!
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		MainSettingsDto dto = om.readValue(file, MainSettingsDto.class);
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
