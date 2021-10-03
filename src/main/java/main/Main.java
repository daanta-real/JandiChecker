package main;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import main.data.DataSystem;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

class Main extends ListenerAdapter {

	private static MessageReceivedEvent ev;

	// 서버의 실행
	public static void main(String[] args) {

		// 서버의 실행

        // 기본 jda를 만들고
        JDA jda = null;
		try {
			jda = JDABuilder.createDefault(DataSystem.TOKEN).build();
		} catch (LoginException e) { e.printStackTrace(); }

        // jda에 이벤트를 감지하는 리스너를 넣는다.
        jda.addEventListener(new Main());

	}

	// 메세지 보냄
	public void send(String msg) { ev.getChannel().sendMessage(msg).queue(); }

	// 명령어에 따른 응답 부분
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

    	// 이벤트 저장
    	ev = event;

    	// 커맨드 분석하기. 첫 번째 String은 cmd, 이후 String은 args이므로 List자료형 opt에 저장.
    	String[] cmds = event.getMessage().getContentRaw().split(" ");
    	String cmd = cmds[0];
    	List<String> opt = new ArrayList<>();
    	for(int i = 1; i < cmds.length; i++) opt.add(cmds[i]);

        // 명령의 실행
    	switch(cmd) {
    		case "!목표": send("저는 매일 저녁과 자정, 잔디를 심지 않은 사람들을 찾아내 그 명단을 발표할 것입니다."); break;
    		case "!확인": break;
    	}

    }
}
