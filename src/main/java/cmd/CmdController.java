package cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chat.ChatService;
import jda.JdaMsgSender;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import init.Initializer;

// 입수된 이벤트 객체로부터
// 명령을 뽑아내어 분석 후, 알맞은 명령을 실행하는 단일 메소드만 있는 클래스
@Slf4j
public class CmdController {

	public static void command(MessageReceivedEvent event) throws Exception {

		log.info("[[명령이 접수되었습니다.]]");
		String id = event.getChannel().getId();
		log.info("채널 ID: " + id);

		// 커맨드 정보 접수부.
		// 명령은 명령과 옵션, 이렇게 두 개의 String을 스페이스로 구분한 형태로 받게 되어 있다.
		// 첫 번째 String은 cmd, 이후 String은 args이므로 List자료형 opt에 저장하도록 되어 있다.

		// 0. 명령문을 split하여 명령정보 배열을 준비
		String[] cmdStrAll = event.getMessage().getContentRaw().split(" ");

		// 1. 명령을 cmd 문자열에 담는다.
		String cmdRawStr = cmdStrAll[0];
		String cmdStarter = Initializer.getCmdChar();
		String cmd = cmdRawStr.substring(cmdStarter.length());
		log.info("접수된 명령문: " + cmd);

		// 2. 옵션을 option 콜렉션에 담는다.
		// 주의: "리스트에 add/remove 등 수정이 필요한 경우에는 Arrays.asList()을 사용해선 안됨을 명심하자."
		// List<String> option = Arrays.asList(cmds) → X
		// List<String> option = new ArrayList(Arrays.asList(cmds)) → O
		// option.removeIf(el -> el.equals(cmds.get(0))); → O but 비추
		// option.stream().filter(el -> el.equals(cmds.get(0))).collect(Collectors.toList()); → O but 비추
		// 아니면 그냥 array 상태에서 remove하는 것도 불편하지만 괜찮다.
		List<String> option = new ArrayList<>(Arrays.asList(cmdStrAll));
		option.remove(0); // 첫 단어는 명령어이므로 빼준다.
		log.info("접수된 옵션 목록: " + option);

		// 본격적인 명령의 분석 및 실행부
		switch(cmd) {

			// 소개말 출력
			case "도움말", "help", "도와줘", "도우미", "도움", "도움!", "소개", "?" -> JdaMsgSender.send(event, Initializer.INFO_STRING);

			// 목표 출력
			case "목표" -> JdaMsgSender.send(event, "매일 자정, 목록에 등재된 인원 중 잔디를 심는데 성공한 사람들을 알려주는 봇입니다.");

			// 특정 별칭에 해당하는 종합 커밋정보 출력
			case "정보" -> JdaMsgSender.send(event, CmdService.showJandiMap(option));

			// 특정 Github ID에 해당하는 종합 커밋정보 출력
			case "id" -> JdaMsgSender.send(event, CmdService.showJandiMapById(option));

			// 현 시점 오늘 커밋 안 한 사람 목록 출력
			case "오늘함" -> JdaMsgSender.send(event, CmdService.showDidCommitToday());

			// 어제 커밋 한 사람 목록 출력
			case "어제" -> JdaMsgSender.send(event, CmdService.showDidCommitYesterday());

			// 어제 커밋 안 한 사람 목록 출력
			case "어제안함" -> JdaMsgSender.send(event, CmdService.showNotCommittedYesterday());

			// 특정 날짜에 잔디를 심은 사람의 목록을 출력
			case "확인" -> JdaMsgSender.send(event, CmdService.showDidCommitSomeday(option));

			// 일반적인 질문에 답하는 AI
			case "질문" -> JdaMsgSender.send(event, ChatService.getChatAnswerByQuestion(option));

		}
	}

}
