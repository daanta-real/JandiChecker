package main;

import javax.security.auth.login.LoginException;

import $.$;
import main.data.MainSystem;
import main.libraries.Cmd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

class Main extends ListenerAdapter {

	// 인스턴스
	// 어디서든 호출할 수 있는 JDA 인스턴스
	public static JDA jda;
	// 싱글톤 인스턴스
	public static MainSystem mainSystem;

	// 환경설정 로드
	public static void readySystem() {

		// mainSystem 변수의 인스턴스 설정
		mainSystem = MainSystem.getInstance();

		// 상세 환경변수 확인 시작
		$.pr(" - 메인 설정 확인: ");
		$.pf(" - 실행 경로 확인: %s\n", mainSystem.getPath());
		$.pf(" - 토큰: %s\n", mainSystem.getToken());
		$.pn(" - 명단 확인: ");
		String[][] members = mainSystem.getMembers();
		for(int i = 0; i < members.length; i++) {
			String[] s = members[i];
			char line = i == members.length - 1 ? '└' : '├';
			$.pf("    %c─ %d번째 인원: '%s' (Github ID: %s)\n", line, i, s[0], s[1]);
		}

	}
	public static void readyJda() {

		// JDA 인스턴스 잡기
		$.pn("\n[[[잔디체커 JDA 인스턴스 생성]]]");
		jda = JdaObj.instance;

		// 기본 jda를 만든다
		try {
			jda = JDABuilder.createDefault(mainSystem.token).build(); // 봇을 만들어 로그인시킨 뒤, JdaObj의 인스턴스 값으로 할당
			$.pn("JDA 인스턴스 생성 완료:" + jda.toString() );
		} catch (LoginException e) { e.printStackTrace(); }

		// jda에 이벤트를 감지하는 리스너 봇을 넣는다.
		$.pn("\n[[[잔디체커 JDA 리스너 로드]]]");
		ListenerAdapter bot = new Main(); // 리스너 봇
		jda.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		$.pn(" - 이벤트 리스너 생성: " + bot.toString());

	}

	// 로딩모듈
	public static void ready() {

		// 환경설정 확인
		$.pn("[[[잔디체커 시작]]]");

		// 환경설정 로드
		$.pn("\n[[[잔디체커 환경설정 로드]]]");
		readySystem();

		// JDA 로드
		$.pn("\n[[[잔디체커 JDA 로드]]]");
		readyJda();

		// 모든 실행 준비가 끝났다.
		$.pn("\n[[[잔디체커 실행 완료]]]");

	}

	// 초기화 이후, 명령어에 따른 동작 실행
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String id = event.getChannel().getId();
		$.pn("[[명령어 입력정보 시작]]");
		$.pn(" - 채널 ID: " + id);
		$.pn("[[명령어 입력정보 끝]]");
		Cmd.command(event);
	}

	// 서버의 실행
	public static void main(String[] args) throws Exception {

		ready();
		/*TextChannel textChannel = YourBotInstance.getJda().getTextChannelById("386242731875368960");
		if(textChannel.canTalk()) {
		    textChannel.sendMessage("Your message here.").queue();
		}*/

	}

}
