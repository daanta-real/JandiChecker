package main.Settings;

// YAML 파일로부터 읽어오는 환경설정 내용들을 담을 YAML DTO
@SuppressWarnings("unused")
public class MainSettingsDto {

	// 환경변수
	private boolean debug; // 디버그 여부
	private String  token; // 토큰
	private String  cron ; // 스케쥴러 실행 주기
	private String[][] members; // 명단

	// Jackson으로 YAML을 읽기 위해서는 DTO에 기본 빈 생성자가 선언은 되어 있어야 한다고 한다.
	// 없으면 에러(Exception)을 낸다고 함.
	public MainSettingsDto() {}

	// Getters / Setters
	public boolean	  isDebug () { return debug; }
	public String	  getToken() { return token; }
	public String	  getCron () { return cron ; }
	public String[][] getMembers() { return members; }
	public void	setDebug(boolean debug) { this.debug = debug; }
	public void	setToken(String  token) { this.token = token; }
	public void	setCron (String  cron ) { this.cron  = cron ; }
	public void setMembers(String[][] members) { this.members = members; }

}