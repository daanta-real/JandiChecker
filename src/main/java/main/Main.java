package main;

import javax.security.auth.login.LoginException;

import $.$;
import main.data.MainSystem;
import main.data.MainSystem;
import main.libraries.Cmd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

class Main extends ListenerAdapter {

	// 어디서든 호출할 수 있는 JDA 생성
    public static JDA jda_obj = null;

	// 명령어에 따른 동작 실행
    @Override
    public void onMessageReceived(MessageReceivedEvent event) { Cmd.command(event); }

	// 서버의 실행
	public static void main(String[] args) throws Exception {

		// 잔디 확인할 대상목록 불러오기
		MainSystem.listReady();

		// 인스턴스 잡기
		JDA jda = JdaObj.instance;
		// 기본 jda를 만든다
		try {
			jda = JDABuilder.createDefault(MainSystem.TOKEN).build(); // 봇을 만들어 로그인시킨 뒤, JdaObj의 인스턴스 값으로 할당
			$.pn("JDA 인스턴스 생성 완료:" + jda.toString() );
		} catch (LoginException e) { e.printStackTrace(); }

		// jda에 이벤트를 감지하는 리스너 봇을 넣는다.
		ListenerAdapter bot = new Main(); // 리스너 봇
		jda.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		$.pn("이벤트 리스너 생성: " + bot.toString());

    }

}