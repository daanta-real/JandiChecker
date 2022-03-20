package main.Cmd;

import java.util.ArrayList;
import java.util.List;

import $.$;
import main.Settings.MainSettings;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

// 입수된 이벤트 객체로부터 명령을 뽑아내어 분석 후, 알맞은 명령을 실행하는 단일 메소드만 있는 클래스
public class Switcher {
	public static void command(MessageReceivedEvent event) throws Exception {

		// 커맨드 정보 접수부.
		// 명령은 명령과 옵션, 이렇게 두 개의 String을 스페이스로 구분한 형태로 받게 되어 있다.
		// 첫 번째 String은 cmd, 이후 String은 args이므로 List자료형 opt에 저장하도록 되어 있다.

		// 0. 명령문을 split하여 명령정보 배열을 준비
		String[] cmds = event.getMessage().getContentRaw().split(" ");

		// 1. 명령을 cmd 문자열에 담는다.
		String cmd = cmds[0];

		// 2. 옵션을 opt 콜렉션에 담는다.
		String option;
		List<String> opt = new ArrayList<String>();
		for(int i = 1; i < cmds.length; i++) opt.add(cmds[i]);

		// 본격적인 명령의 분석 및 실행부
		switch(cmd) {

			// 소개말 출력
			case "&도움말": case "&help": case "&도와줘": case "&도우미": case "&도움": case "&도움!": case "&소개":
				Sender.send(event, MainSettings.INFO_STRING);
				break;

			// 목표 출력
			case "&목표":
				Sender.send(event, "저는 매일 저녁과 자정, 잔디를 심지 않은 사람들을 찾아내 그 명단을 발표할 것입니다.");
				break;

			// 특정 별칭에 해당하는 종합 커밋정보 출력
			case "&정보":
				if(opt.size() == 0) { Sender.send(event, "정확히 입력해 주세요."); break; } // 미입력 걸러내기
				option = opt.get(0);
				$.pn(option + "님 (ID: " + Commands.getGithubID(option) + ")의 정보 호출을 명령받았습니다.");
				Sender.send(event, Commands.showJandiMap(option));
				break;

			// 특정 Github ID에 해당하는 종합 커밋정보 출력
			case "&id":
				if(opt.size() == 0) { Sender.send(event, "정확히 입력해 주세요."); break; } // 미입력 걸러내기
				option = opt.get(0);
				$.pn("ID " + option + " 의 정보 호출을 명령받았습니다.");
				Sender.send(event, Commands.showJandiMapById(option));
				break;

			// 어제 커밋 안 한 사람 목록 출력
			case "&어제안함":
				Sender.send(event, Commands.showNotCommitedYesterday());
				break;

			// 어제 커밋 한 사람 목록 출력
			case "&어제":
				Sender.send(event, Commands.showDidCommitYesterday());
				break;

			// 현 시점 오늘 커밋 안 한 사람 목록 출력
			case "&오늘안함":
				Sender.send(event, Commands.showNotCommitedToday());
				break;

			// 특정 날짜에 잔디를 심지 않은 사람의 목록을 출력
			case "&확인":
				if(opt.size() == 0) { Sender.send(event, "정확히 입력해 주세요."); break; } // 미입력 걸러내기
				option = opt.get(0);
				Sender.send(event, Commands.showNotCommitedSomeday(option));
				break;

			default: break;

		}
	}
}
