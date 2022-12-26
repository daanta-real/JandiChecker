package init;

// YAML 파일로부터 읽어오는 환경설정 내용들을 담을 YAML DTO
public class InitializerSettingsFileVO {

	// 환경변수
	private final String token; // 토큰
	private final String cron ; // 스케쥴러 실행 주기
	private final String[][] members; // 명단
	private final String targetChannelId; // CRON 스케쥴러 실행 결과 메세지가 전송될 타겟 채널 ID

	// Jackson으로 YAML을 읽기 위해서는 DTO에 기본 빈 생성자가 선언은 되어 있어야 한다고 한다.
	// 없으면 에러(Exception)을 낸다고 함.
	public InitializerSettingsFileVO() {
		this.token = null;
		this.cron = null;
		this.members = null;
		this.targetChannelId = null;
	}

	// Getters
	public String	  getToken() { return token; }
	public String	  getCron () { return cron ; }
	public String[][] getMembers() { return members; }
	public String     getTargetChannelId() { return targetChannelId; }

}