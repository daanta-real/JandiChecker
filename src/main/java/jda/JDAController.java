package jda;

import jda.interaction.*;
import jda.menu.SlashMenu;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import init.Initializer;

import java.util.List;

@Slf4j
public class JDAController extends ListenerAdapter {

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Fields
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static JDA instance;
	public static final String CMD_ME = "나";
	public static final String CMD_JANDIYA = "잔디야";
	public static final String CMD_NAME = "정보";
	public static final String CMD_ID = "id";
	public static final String CMD_LIST_YESTERDAY_SUCCESS = "어제";
	public static final String CMD_LIST_YESTERDAY_FAIL = "어제안함";
	public static final String CMD_LIST_TODAY_SUCCESS = "오늘";
	public static final String CMD_LIST_BY_DATE = "날짜";
	public static final String CMD_ABOUT = "대하여";
	public static final String CMD_CLOSE = "닫기";



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Initializer
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void init() {

		// JDA의 기본 인스턴스를 만든다.
		log.info("");
		log.info("[잔디체커 JDA 인스턴스 생성]");
		// 원래 옛날 버전에서는 여기를 try catch를 감싸서 예외처리를 해야 했으나 패치로 없어진 모양
		instance = JDABuilder.createDefault(Initializer.getToken_discordBot())
				.enableIntents(GatewayIntent.MESSAGE_CONTENT) // JDA 4.2.0부터 정책이 바뀌어 권한을 직접 활성화해줘야 함
				.build(); // 봇을 만들어 로그인시킨 뒤, JdaObj의 인스턴스 값으로 할당
		log.info("JDA 인스턴스 생성 완료:" + instance);

		// jda에 이벤트를 감지하는 리스너 봇을 넣는다.
		log.info("");
		log.info("[잔디체커 JDA 리스너 로드]");
		ListenerAdapter bot = new JDAController(); // JDA 봇 객체. 리스너이기도 하다
		instance.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		log.info("이벤트 리스너 생성: " + bot);

		// 슬래시 커맨드 추가
		log.info("");
		log.info("[잔디체커 슬래시 커맨드 로드]");
		List<CommandData> cmdList = SlashMenu.getMenusList();
		instance.updateCommands().addCommands(cmdList).queue();
		log.info("슬래시 커맨드 추가 완료.");

	}



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Listeners
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		MessageInteraction.run(event);
	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		SlashInteraction.run(event);
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		ButtonInteraction.run(event);
	}

	@Override
	public void onModalInteraction(@NotNull ModalInteractionEvent event) {
		ModalInteraction.run(event);
	}

}
