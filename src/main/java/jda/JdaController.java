package jda;

import chat.ChatService;
import cmd.ButtonMenues;
import cmd.CmdService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import init.Initializer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdaController extends ListenerAdapter {

	// 어디서든 호출할 수 있는 JDA 인스턴스
	public static JDA instance;

	// Command constant Strings
	public static final String CMD_ME = "나";
	public static final String CMD_JANDIYA = "잔디야";
	public static final String CMD_NAME = "정보";
	public static final String CMD_ID = "id";
	public static final String CMD_LIST_YESTERDAY_SUCCESS = "어제";
	public static final String CMD_LIST_YESTERDAY_FAIL = "어제안함";
	public static final String CMD_LIST_BY_DATE = "날짜";
	public static final String CMD_ABOUT = "대하여";
	public static final String CMD_CLOSE = "닫기";

	// Initializer
	public static void init() {

		// JDA 인스턴스 잡기
		log.info("");
		log.info("[잔디체커 JDA 인스턴스 생성]");

		// 기본 jda를 만든다
		// 원래 옛날 버전에서 try catch를 써서 예외처리를 해야 했으나 패치로 없어진 모양
		instance = JDABuilder.createDefault(Initializer.getToken_discordBot())
				.enableIntents(GatewayIntent.MESSAGE_CONTENT) // JDA 4.2.0부터 정책이 바뀌어 권한을 직접 활성화해줘야 함
				.build(); // 봇을 만들어 로그인시킨 뒤, JdaObj의 인스턴스 값으로 할당
		log.info("JDA 인스턴스 생성 완료:" + instance);

		// jda에 이벤트를 감지하는 리스너 봇을 넣는다.
		log.info("");
		log.info("[잔디체커 JDA 리스너 로드]");
		ListenerAdapter bot = new JdaController(); // JDA 봇 객체. 리스너이기도 하다
		instance.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		log.info("이벤트 리스너 생성: " + bot);

		// 슬래시 커맨드 추가
		List<CommandData> cmdList = new ArrayList<>();
		cmdList.add(Commands.slash(CMD_ME, "내 잔디 정보를 조회합니다."));
		cmdList.add(Commands.slash(CMD_JANDIYA, "잔디의 친구 ChatGPT AI에게 질문을 해봅니다.")
				.addOption(OptionType.STRING, "option", "질문을 입력해 주세요.", true)
		);
		cmdList.add(Commands.slash(CMD_NAME, "그룹원의 이름을 입력하여 그룹원의 잔디 정보를 조회합니다.")
				.addOption(OptionType.STRING, "option", "그룹원의 이름을 입력해 주세요.", true)
		);
		cmdList.add(Commands.slash(CMD_ID, "특정 ID의 잔디 정보를 조회합니다. 그룹원이 아닌 사람도 조회 가능합니다.")
				.addOption(OptionType.STRING, "option", "조회하고자 하는 사람의 id를 입력하세요.", true)
		);
		cmdList.add(Commands.slash(CMD_LIST_YESTERDAY_SUCCESS, "어제 잔디를 심는데 성공한 그룹원 목록을 확인합니다."));
		cmdList.add(Commands.slash(CMD_LIST_YESTERDAY_FAIL, "어제 잔디 심기를 깜박한 그룹원 목록을 확인합니다."));
		cmdList.add(Commands.slash(CMD_LIST_BY_DATE, "특정 날짜에 잔디를 심는데 성공한 그룹원 목록을 확인합니다.")
				.addOption(OptionType.STRING, "option", "날짜를 입력하세요. yyyyMMdd 형태로 입력하셔야 합니다.", true)
		);
		cmdList.add(Commands.slash(CMD_ABOUT, "이 봇에 대한 소개와 도움말을 출력합니다."));
		instance.updateCommands().addCommands(cmdList).queue();

	}

	// 초기화 이후, 슬래시를 사용하지 않은 메세지 발생에 따른 동작 실행
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {

		// 메세지 입수
		String totalString = event.getMessage().getContentRaw();

		// 접수된 메세지가 비었을 경우 리턴.
		if(totalString.isBlank()) return;

		// 명령어에 따라 다르게 동작
		if(totalString.equals("잔디야")) {
			// "잔디야" 라고만 치면 메뉴판 호출 후 리턴
			ButtonMenues.showButtonMenues(event);
		} else if(totalString.startsWith("잔디야 ")) {
			String question = totalString.substring(4);
			JdaMsgSender.send(event, ChatService.getChatAnswerByQuestion(event, question)); // 메세지를 바로 돌려준다
		}

	}

	// 초기화 이후, 슬래시를 사용한 메세지 발생(=커맨드 입력)에 따른 동작 실행
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

		// Print the loading spinner
		event.deferReply().queue();

		// Get command and options
		String cmd = event.getName(); // Command
		OptionMapping o = event.getOption("option");
		String option = o != null ? o.getAsString() : ""; // Option string
		log.debug("[[[ 슬래시 명령문 입력으로 명령을 접수하였습니다. ]]] [{}: '{}']", cmd, option);

		// Make result
		String result;
		try {
			result = switch (cmd) {
					case CMD_ME                     -> CmdService.showJandiMapOfMe(event); // 내 잔디정보를 출력
					case CMD_JANDIYA                -> ChatService.getChatAnswerByQuestion(event, option); // 일반적인 질문에 답하는 AI
					case CMD_NAME                   -> CmdService.showJandiMapByName(option); // 특정 이름의 그룹원의 종합 잔디정보 출력
					case CMD_ID                     -> CmdService.showJandiMapById(option); // 특정 Github ID의 종합 잔디정보 출력
					case CMD_LIST_YESTERDAY_FAIL    -> CmdService.showNotCommittedYesterday(); // 어제 잔디심기 안 한 사람 목록 출력
					case CMD_LIST_BY_DATE           -> CmdService.showDidCommitSomeday(option); // 특정 날짜에 잔디를 심은 사람의 목록을 출력
					case CMD_ABOUT                  -> Initializer.INFO_STRING; // 소개말
					default -> throw new Exception();
				};
		} catch (Exception e) {
			result = "정보 획득에 실패하였습니다.";
		}

		// Send
		event.getHook().sendMessage(result).queue();

	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		String cmd = event.getComponentId();
		log.debug("[[[ 버튼 누르기로 명령을 접수하였습니다. ]]] [{}]", cmd);
		if (event.getComponentId().equals("hello")) {
			event.reply("Hello :)").queue(); // send a message in the channel
		} else if (event.getComponentId().equals("emoji")) {
			event.editMessage("That button didn't say click me").queue(); // update the message
		} else {
			event.editMessage("버튼눌렀찌?").queue(); // update the message
		}
//		if (event.getName().equals("hello")) {
//			event.reply("Click the button to say hello")
//					.addActionRow(
//							Button.primary("hello", "Click Me"), // Button with only a label
//							Button.success("emoji", Emoji.fromFormatted("<:minn:245267426227388416>"))) // Button with only an emoji
//					.queue();
//		} else if (event.getName().equals("info")) {
//			event.reply("Click the buttons for more info")
//					.addActionRow( // link buttons don't send events, they just open a link in the browser when clicked
//							Button.link("https://github.com/DV8FromTheWorld/JDA", "GitHub")
//									.withEmoji(Emoji.fromFormatted("<:github:849286315580719104>")), // Link Button with label and emoji
//							Button.link("https://ci.dv8tion.net/job/JDA/javadoc/", "Javadocs")) // Link Button with only a label
//					.queue();
//		}
	}

}
