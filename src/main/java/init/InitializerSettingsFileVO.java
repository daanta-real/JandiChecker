package init;

// YAML 파일로부터 읽어오는 환경설정 내용들을 담을 YAML DTO
public class InitializerSettingsFileVO {

	// 환경변수
	private final String tokenJDA; // 토큰
	private final String cron ; // 스케쥴러 실행 주기
	private final String[][] members; // 명단
	private final String targetChannelId; // CRON 스케쥴러 실행 결과 메세지가 전송될 타겟 채널 ID
	private final String tokenGoogleTranslateAPI; // 구글 번역 API 토큰
	private final String tokenChatGPTAPI; // ChatGPT API 토큰

	// Jackson으로 YAML을 읽기 위해서는 DTO에 기본 빈 생성자가 선언은 되어 있어야 한다고 한다.
	// 없으면 에러(Exception)을 낸다고 함.
	public InitializerSettingsFileVO() {
		this.tokenJDA = null;
		this.cron = null;
		this.members = null;
		this.targetChannelId = null;
		this.tokenGoogleTranslateAPI = null;
		this.tokenChatGPTAPI = null;
	}

	// Getters
	public String getTokenJDA() { return tokenJDA; }
	public String getCron () { return cron ; }
	public String[][] getMembers() { return members; }
	public String getTargetChannelId() { return targetChannelId; }
	public String getTokenGoogleTranslateAPI() { return tokenGoogleTranslateAPI; }
	public String getTokenChatGPTAPI() { return tokenChatGPTAPI; }

}
