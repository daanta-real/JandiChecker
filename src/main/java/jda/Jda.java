package jda;

import $.$;
import cmd.Switcher;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import settings.MainSettings;

public class Jda extends ListenerAdapter {

	// 어디서든 호출할 수 있는 JDA 인스턴스
	public static JDA instance;

	public static void load() {

		// JDA 인스턴스 잡기
		$.pn("\n[[[잔디체커 JDA 인스턴스 생성]]]\n");

		// 기본 jda를 만든다
		// 원래 옛날 버전에서 throw가 있었으나 패치로 없어진 모양
		instance = JDABuilder.createDefault(MainSettings.getToken()).build(); // 봇을 만들어 로그인시킨 뒤, JdaObj의 인스턴스 값으로 할당
		$.pn("JDA 인스턴스 생성 완료:" + instance );

		// jda에 이벤트를 감지하는 리스너 봇을 넣는다.
		$.pn("\n[[[잔디체커 JDA 리스너 로드]]]");
		ListenerAdapter bot = new Jda(); // JDA 봇 객체. 리스너이기도 하다
		instance.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		$.pn(" - 이벤트 리스너 생성: " + bot);

	}

	// 초기화 이후, 명령어에 따른 동작 실행
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {

		// 명령 실행
		try { Switcher.command(event); } catch (Exception e) { e.printStackTrace(); }

	}

}
