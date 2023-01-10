package jda;

import chat.ChatService;
import cmd.ButtonMenues;
import cmd.CmdController;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import init.Initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JdaController extends ListenerAdapter {

	// 어디서든 호출할 수 있는 JDA 인스턴스
	public static JDA instance;

	// Command constant Strings
	public static final String CMD_ME = "나";
	public static final String CMD_NAME = "정보";
	public static final String CMD_ID = "id";
	public static final String CMD_LIST_TODAY_SUCCESS = "오늘";
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
		cmdList.add(Commands.slash(CMD_ME, "내 커밋 정보를 조회합니다."));
		cmdList.add(Commands.slash(CMD_NAME, "그룹원의 이름을 입력하여 그룹원의 커밋 정보를 조회합니다."));
		cmdList.add(Commands.slash(CMD_ID, "특정 ID의 커밋 정보를 조회합니다. 그룹원이 아닌 사람도 조회 가능합니다."));
		cmdList.add(Commands.slash(CMD_LIST_TODAY_SUCCESS, "오늘 잔디를 심는데 성공한 그룹원 목록을 확인합니다."));
		cmdList.add(Commands.slash(CMD_LIST_YESTERDAY_SUCCESS, "어제 잔디를 심는데 성공한 그룹원 목록을 확인합니다."));
		cmdList.add(Commands.slash(CMD_LIST_YESTERDAY_FAIL, "어제 잔디 심기를 깜박한 그룹원 목록을 확인합니다."));
		cmdList.add(Commands.slash(CMD_LIST_BY_DATE, "특정 날짜에 잔디를 심는데 성공한 그룹원 목록을 확인합니다."));
		cmdList.add(Commands.slash(CMD_ABOUT, "이 봇에 대한 소개와 도움말을 출력합니다."));
		instance.updateCommands().addCommands(cmdList).queue();

	}

	// 초기화 이후, 메세지 발생에 따른 동작 실행
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {

		// 메세지 입수
		String totalString = event.getMessage().getContentRaw();

		// 접수된 메세지가 비었을 경우 리턴.
		if(totalString.isBlank()) return;

		// case 1. 버튼형 메뉴를 불러올 경우 커맨드를 따지지 않고 바로 메뉴판 호출 후 리턴
		if(totalString.equals("잔디야")) {
			ButtonMenues.showButtonMenues(event);
			return;
		}

		// case 2. ChatGPT 메뉴일 경우 채팅 실행
		if(totalString.length() >= 5 && totalString.startsWith("잔디야 ")) {
			String question = totalString.substring(4);
			JdaMsgSender.send(event, ChatService.getChatAnswerByQuestion(question));
		}

		// case 3. 여기서부터 구형 커맨드..

		// 메세지가 없거나, 그 첫 글자가 &가 아니어서 명령이 아닌 경우, 무지성 리턴
		String firstString = totalString.substring(0, 1); // Trim the first char - Identifier &
		if(!"&".equals(firstString)) return;

		// 명령 실행
		try {
			CmdController.command(event);
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}

	}

	// 초기화 이후, 슬래시 입력 시 동작 실행
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

		// Print loading spinner
		event.deferReply().queue();
		String option;
		try {
			option = Objects.requireNonNull(event.getOption("content")).getAsString();
		} catch(NullPointerException e) {
			option = "";
		}
		event.reply("슬래시다! 옵션: '" + option + "'").queue(); // reply immediately
		// Get command and options
		String cmd = event.getName();
		log.debug("<<<슬래시 명령문 입력으로 명령을 접수하였습니다.>>> [{}: {}]", cmd, option);


		if (event.getName().equals("hello")) {
			event.reply("Click the button to say hello")
					.addActionRow(
							Button.primary("hello", "Click Me"), // Button with only a label
							Button.success("emoji", Emoji.fromFormatted("<:minn:245267426227388416>"))) // Button with only an emoji
					.queue();
		} else if (event.getName().equals("info")) {
			event.reply("Click the buttons for more info")
					.addActionRow( // link buttons don't send events, they just open a link in the browser when clicked
							Button.link("https://github.com/DV8FromTheWorld/JDA", "GitHub")
									.withEmoji(Emoji.fromFormatted("<:github:849286315580719104>")), // Link Button with label and emoji
							Button.link("https://ci.dv8tion.net/job/JDA/javadoc/", "Javadocs")) // Link Button with only a label
					.queue();
		}
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		String cmd = event.getComponentId();
		log.debug("<<<버튼 입력으로 명령을 접수하였습니다.>>> [{}]", cmd);
		if (event.getComponentId().equals("hello")) {
			event.reply("Hello :)").queue(); // send a message in the channel
		} else if (event.getComponentId().equals("emoji")) {
			event.editMessage("That button didn't say click me").queue(); // update the message
		} else {
			event.editMessage("버튼눌렀찌?").queue(); // update the message
		}
	}

}
