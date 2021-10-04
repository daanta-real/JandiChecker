package main;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import $.$;
import main.data.DataSystem;
import main.libraries.Cmd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

class BotMain extends ListenerAdapter {

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
        jda.addEventListener(new BotMain());

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
    		case "$$도움말": case "$$help": case "$$도와줘": case "$$도우미": case "$$도움": case "$$도움!": case "$$소개":
    			send("```md\n_**저는 매일 저녁과 자정, 잔디를 심지 않은 사람들을 찾아내 그 명단을 발표할 것입니다.**_\n"
					+ "$$목표: 이 봇이 제작된 목표를 설명합니다.\n"
					+ "$$정보 [사람이름]: 특정인의 최근 1년 간 및 근 30일 간의 Github 잔디 정보를 가져옵니다. 이때 관리목록에 이름이 서로 겹치는 인원이 없을 경우, 성은 생략해도 무관합니다.\n"
					+ "$$id [id]: 특정 id의 최근 1년 간 및 근 30일 간의 Github 잔디 정보를 가져옵니다.\n"
					+ "$$어제: 어제 잔디를 제출하지 않은 사람들의 명단을 공개합니다.\n"
					+ "$$오늘: 오늘 잔디를 제출하지 않은 사람들의 명단을 공개합니다.\n"
					+ "$$확인 [날짜(yyyy-MM-dd 형식)]: 특정 날짜에 잔디를 제출하지 않은 사람들의 명단을 출력합니다.\n"
					+ "\n"
					+ "잔디체커(JandiChecker) v1.0\n제작 by 단타(박준성)\ne-mail: daanta@naver.com\nGithub: http://github.com/daanta-real```");
    			break;
    		case "$$목표": send("저는 매일 저녁과 자정, 잔디를 심지 않은 사람들을 찾아내 그 명단을 발표할 것입니다.");
    			break;
    		case "$$정보":
    			try {
    				if(opt.size() == 0) { send("정확히 입력해 주세요."); break; } // 미입력 걸러내기
    				String option = opt.get(0);
    				$.pn(option + "님 (ID: " + Cmd.getGithubID(option) + ")의 정보 호출을 명령받았습니다.");
    				send(Cmd.showJandiMap(option));
				} catch (Exception e) { e.printStackTrace(); }
    		break;
    		case "$$id":
    			try {
    				if(opt.size() == 0) { send("정확히 입력해 주세요."); break; } // 미입력 걸러내기
    				String option = opt.get(0);
    				$.pn("ID " + option + " 의 정보 호출을 명령받았습니다.");
    				send(Cmd.showJandiMapById(option));
				} catch (Exception e) { e.printStackTrace(); }
    		break;
    		case "$$어제":
    			try { send(Cmd.showNotCommitedYesterday()); }
    			catch (Exception e) { e.printStackTrace(); }
			break;
    		case "$$오늘":
    			try { send(Cmd.showNotCommitedToday()); }
    			catch (Exception e) { e.printStackTrace(); }
			break;
    		case "$$확인":
    			try {
    				if(opt.size() == 0) { send("정확히 입력해 주세요."); break; } // 미입력 걸러내기
    				String option = opt.get(0);
    				send(Cmd.showNotCommitedSomeday(option));
    			} catch (Exception e) { e.printStackTrace(); }
			break;
			default: break;
    	}

    }
}
