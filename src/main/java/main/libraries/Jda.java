package main.libraries;

import javax.security.auth.login.LoginException;

import $.$;
import main.data.MainSystem;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Jda extends ListenerAdapter {

	// 어디서든 호출할 수 있는 JDA 인스턴스
	private static JDA instance;

	// Jda 봇 객체
	private static ListenerAdapter bot;

	public static void load() {

		// JDA 인스턴스 잡기
		$.pn("\n[[[잔디체커 JDA 인스턴스 생성]]]\n");

		// 기본 jda를 만든다
		try {
			instance = JDABuilder.createDefault(MainSystem.TOKEN).build(); // 봇을 만들어 로그인시킨 뒤, JdaObj의 인스턴스 값으로 할당
			$.pn("JDA 인스턴스 생성 완료:" + instance.toString() );
		} catch (LoginException e) { e.printStackTrace(); }

		// jda에 이벤트를 감지하는 리스너 봇을 넣는다.
		$.pn("\n[[[잔디체커 JDA 리스너 로드]]]");
		bot = new Jda(); // 리스너 봇
		instance.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		$.pn(" - 이벤트 리스너 생성: " + bot.toString());

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

}
