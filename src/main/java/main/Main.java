package main;

import javax.security.auth.login.LoginException;

import main.data.SecretData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

class Main extends ListenerAdapter {

	// 서버의 실행
	public static void main(String[] args) {

		// 서버의 실행

        // 기본 jda를 만들고
        JDA jda = null;
		try {
			jda = JDABuilder.createDefault(SecretData.TOKEN).build();
		} catch (LoginException e) { e.printStackTrace(); }

        // jda에 이벤트를 감지하는 리스너를 넣는다.
        jda.addEventListener(new Main());

	}

	// 명령어에 따른 응답 부분
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // 받은 메세지 내용이 !ping이라면
        if (event.getMessage().getContentRaw().equals("!목표")) {
            // pong라는 내용을 보낸다.
            event.getChannel().sendMessage("저는 오늘 잔디를 심지 않은 사람들의 명단을 발굴하여 이를 공개할 것입니다.").queue();
        }
    }
}
