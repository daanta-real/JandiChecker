package cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.tools.javac.Main;
import window.WindowService;
import jda.JdaMsgSender;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import configurations.Configurations;

// 입수된 이벤트 객체로부터
// 명령을 뽑아내어 분석 후, 알맞은 명령을 실행하는 단일 메소드만 있는 클래스
@Slf4j
public class CommandController {

	public static void command(MessageReceivedEvent event) throws Exception {

		// 접수된 메세지가 명령문이 아닐 경우 리턴.
		// 리스닝 중인 채널에 메세지가 발생하였으나, 메세지가 없거나, 그 첫 글자가 &가 아니어서 명령이 아닌 경우, 무지성 리턴
		String totalString = event.getMessage().getContentRaw();
		if(totalString.isBlank()) return;
		String firstString = totalString.substring(0, 1); // Trim the first char - Identifier &
		if(!"&".equals(firstString)) return;
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
		String cmdStarter = Configurations.getCmdChar();
		String cmd = cmdRawStr.substring(cmdStarter.length());
		log.info("접수된 명령문: " + cmd);

		// 2. 옵션을 opt 콜렉션에 담는다.
		// 주의: "리스트에 add/remove 등 수정이 필요한 경우에는 Arrays.asList()을 사용해선 안됨을 명심하자."
		// List<String> opt = Arrays.asList(cmds) → X
		// List<String> opt = new ArrayList(Arrays.asList(cmds)) → O
		// opt.removeIf(el -> el.equals(cmds.get(0))); → O but 비추
		// opt.stream().filter(el -> el.equals(cmds.get(0))).collect(Collectors.toList()); → O but 비추
		// 아니면 그냥 array 상태에서 remove하는 것도 불편하지만 괜찮다.
		String option;
		List<String> opt = new ArrayList<>(Arrays.asList(cmdStrAll));
		opt.remove(0); // 첫 단어는 명령어이므로 빼준다.
		log.info("접수된 옵션 목록: " + opt);

		// 본격적인 명령의 분석 및 실행부
		switch(cmd) {

			// 소개말 출력
			case "도움말", "help", "도와줘", "도우미", "도움", "도움!", "소개", "?" -> JdaMsgSender.send(event, Configurations.INFO_STRING);

			// 목표 출력
			case "목표" -> JdaMsgSender.send(event, "매일 자정, 목록에 등재된 인원 중 잔디를 심는데 성공한 사람들을 알려주는 봇입니다.");

			// 특정 별칭에 해당하는 종합 커밋정보 출력
			case "정보" -> {
				if (opt.size() == 0) {
					JdaMsgSender.send(event, "정확히 입력해 주세요.");
					break;
				} // 미입력 걸러내기
				option = opt.get(0);
				log.info(option + "님 (ID: " + CommandService.getGithubID(option) + ")의 정보 호출을 명령받았습니다.");
				JdaMsgSender.send(event, CommandService.showJandiMap(option));
			}

			// 특정 Github ID에 해당하는 종합 커밋정보 출력
			case "id" -> {
				if (opt.size() == 0) {
					JdaMsgSender.send(event, "정확히 입력해 주세요.");
					break;
				} // 미입력 걸러내기
				option = opt.get(0);
				log.info("ID " + option + " 의 정보 호출을 명령받았습니다.");
				JdaMsgSender.send(event, CommandService.showJandiMapById(option));
			}

			// 어제 커밋 안 한 사람 목록 출력
			case "어제안함" -> JdaMsgSender.send(event, CommandService.showNotCommitedYesterday());

			// 어제 커밋 한 사람 목록 출력
			case "어제" -> JdaMsgSender.send(event, CommandService.showDidCommitYesterday());

			// 현 시점 오늘 커밋 안 한 사람 목록 출력
			case "&오늘안함" -> JdaMsgSender.send(event, CommandService.showNotCommitedToday());

			// 특정 날짜에 잔디를 심지 않은 사람의 목록을 출력
			case "확인" -> {
				// 미입력 걸러내기
				if (opt.size() == 0) {
					JdaMsgSender.send(event, "정확히 입력해 주세요.");
					break;
				}
				option = opt.get(0);
				JdaMsgSender.send(event, CommandService.showNotCommitedSomeday(option));
			}

			case "숨김" -> Main.goTray();

		}
	}
}
