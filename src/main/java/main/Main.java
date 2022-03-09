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

	// 어디서든 호출할 수 있는 JDA 생성
	public static JDA jda_obj = null;

	// 싱글톤 인스턴스 획득
	public static MainSystem mainSystem = MainSystem.getInstance();


	// 명령어에 따른 동작 실행
	@Override
	public void onMessageReceived(MessageReceivedEvent event) { Cmd.command(event); }

	// 서버의 실행
	public static void main(String[] args) throws Exception {

		// 환경설정 확인
		$.pn("[[[잔디체커 환경설정 확인]]]");
		$.pf(" - 실행 경로 확인: %s\n", mainSystem.getPath());
		$.pf(" - 토큰: %s\n", mainSystem.getToken());
		$.pn(" - 명단 확인: ");
		String[][] members = mainSystem.getMembers();
		for(int i = 0; i < members.length; i++) {
			String[] s = members[i];
			$.pf("	ㄴ %d번째 인원: '%s' (Github ID: %s)", s[0], s[1]);
		}

		// 인스턴스 잡기
		$.pn("[[[잔디체커 JDA 인스턴스 생성]]]");
		JDA jda = JdaObj.instance;
		// 기본 jda를 만든다
		try {
			jda = JDABuilder.createDefault(mainSystem.token).build(); // 봇을 만들어 로그인시킨 뒤, JdaObj의 인스턴스 값으로 할당
			$.pn("JDA 인스턴스 생성 완료:" + jda.toString() );
		} catch (LoginException e) { e.printStackTrace(); }

		// jda에 이벤트를 감지하는 리스너 봇을 넣는다.
		$.pn("[[[잔디체커 JDA 리스너 로드]]]");
		ListenerAdapter bot = new Main(); // 리스너 봇
		jda.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		$.pn(" - 이벤트 리스너 생성: " + bot.toString());

		// 모든 실행 준비가 끝났다.
		$.pn("[[[잔디체커 실행 완료]]]");

	}

}
