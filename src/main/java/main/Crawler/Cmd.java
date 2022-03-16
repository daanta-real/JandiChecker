package main.Crawler;

import java.util.ArrayList;
import java.util.List;

import $.$;
import main.Settings.MainSettings;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Cmd {

	// 메세지 보냄
	// 채널명으로
	public static void send(String channelId, String msg) {
//		try {
//
//		}
//		event.getChannel().sendMessage(msg).queue();
	}
	// 이벤트가 들어왔을 때 메세지 전송
	public static void send(MessageReceivedEvent event, String msg) { event.getChannel().sendMessage(msg).queue(); }
	// 메세지 보내는 원본 메소드
	public static void send(TextChannel channel, String msg) {
		$.pn(channel.canTalk());
		if(channel.canTalk()) channel.sendMessage(msg).queue();
		else $.pn("메세지를 보낼 수가 없어요");
	}

	// 1명의 깃헙 ID를 획득
	public static String getGithubID(String name) {
		for(String[] s: MainSettings.getMembers())
			if(s[0].equals(name) || s[0].substring(1).equals(name)) return s[1];
		return null;
	}

	// 1명의 깃헙 정보를 출력
	public static String showJandiMap(String name) throws Exception {
		String id = getGithubID(name);
		if(id == null) return "없는 스터디원입니다.";
		else return GithubMap.getGithubInfoString(name, id);
	}

	// 특정 ID의 깃헙 정보를 출력
	public static String showJandiMapById(String id) throws Exception {
		return GithubMap.getGithubInfoString("http://github.com/" + id + "/", id);
	}

	// 어제 커밋 안 한 스터디원 목록을 출력
	public static String showNotCommitedYesterday() throws Exception {
		return Checker.getNotCommitedYesterday();
	}

	// 지금 커밋 안 한 스터디원 목록을 출력
	public static String showNotCommitedToday() throws Exception {
		return Checker.getNotCommitedToday();
	}

	// 특정일에 커밋 안 한 스터디원 목록을 출력
	public static String showNotCommitedSomeday(String date) throws Exception {
		return Checker.getNotCommitedSomeday(date);
	}

	public static void command(MessageReceivedEvent event) {

    	// 커맨드 분석하기. 첫 번째 String은 cmd, 이후 String은 args이므로 List자료형 opt에 저장.
    	String[] cmds = event.getMessage().getContentRaw().split(" ");
    	String cmd = cmds[0];
    	List<String> opt = new ArrayList<String>();
    	for(int i = 1; i < cmds.length; i++) opt.add(cmds[i]);

		// 명령의 실행
    	switch(cmd) {
    		case "&도움말": case "&help": case "&도와줘": case "&도우미": case "&도움": case "&도움!": case "&소개":
    			send(event, "```md\n_**저는 매일 저녁과 자정, 잔디를 심지 않은 사람들을 찾아내 그 명단을 발표할 것입니다.**_\n"
					+ "&목표: 이 봇이 제작된 목표를 설명합니다.\n"
					+ "&정보 [사람이름]: 특정인의 최근 1년 간 및 근 30일 간의 Github 잔디 정보를 가져옵니다. 이때 관리목록에 이름이 서로 겹치는 인원이 없을 경우, 성은 생략해도 무관합니다.\n"
					+ "&id [id]: 특정 id의 최근 1년 간 및 근 30일 간의 Github 잔디 정보를 가져옵니다.\n"
					+ "&어제: 어제 잔디를 제출하지 않은 사람들의 명단을 공개합니다.\n"
					+ "&오늘: 오늘 잔디를 제출하지 않은 사람들의 명단을 공개합니다.\n"
					+ "&확인 [날짜(yyyy-MM-dd 형식)]: 특정 날짜에 잔디를 제출하지 않은 사람들의 명단을 출력합니다.\n"
					+ "\n"
					+ "잔디체커(JandiChecker) v1.0\n제작 by 단타(박준성)\ne-mail: daanta@naver.com\nGithub: http://github.com/daanta-real```");
    			break;
    		case "&목표": send(event, "저는 매일 저녁과 자정, 잔디를 심지 않은 사람들을 찾아내 그 명단을 발표할 것입니다.");
    			break;
    		case "&정보":
    			try {
    				if(opt.size() == 0) { send(event, "정확히 입력해 주세요."); break; } // 미입력 걸러내기
    				String option = opt.get(0);
    				$.pn(option + "님 (ID: " + Cmd.getGithubID(option) + ")의 정보 호출을 명령받았습니다.");
    				send(event, Cmd.showJandiMap(option));
				} catch (Exception e) { e.printStackTrace(); }
    		break;
    		case "&id":
    			try {
    				if(opt.size() == 0) { send(event, "정확히 입력해 주세요."); break; } // 미입력 걸러내기
    				String option = opt.get(0);
    				$.pn("ID " + option + " 의 정보 호출을 명령받았습니다.");
    				send(event, Cmd.showJandiMapById(option));
				} catch (Exception e) { e.printStackTrace(); }
    		break;
    		case "&어제":
    			try { send(event, Cmd.showNotCommitedYesterday()); }
    			catch (Exception e) { e.printStackTrace(); }
			break;
    		case "&오늘":
    			try { send(event, Cmd.showNotCommitedToday()); }
    			catch (Exception e) { e.printStackTrace(); }
			break;
    		case "&확인":
    			try {
    				if(opt.size() == 0) { send(event, "정확히 입력해 주세요."); break; } // 미입력 걸러내기
    				String option = opt.get(0);
    				send(event, Cmd.showNotCommitedSomeday(option));
    			} catch (Exception e) { e.printStackTrace(); }
			break;
			default: break;
    	}
	}
}