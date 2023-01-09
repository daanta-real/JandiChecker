package jda;

import cmd.CmdController;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import init.Initializer;

@Slf4j
public class JdaController extends ListenerAdapter {

	// 어디서든 호출할 수 있는 JDA 인스턴스
	public static JDA instance;

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
		instance.updateCommands().addCommands(
				Commands.slash("echo", "Repeats messages back to you.")
						.addOption(OptionType.STRING, "message", "The message to repeat.")
						.addOption(OptionType.INTEGER, "times", "The number of times to repeat the message.")
						.addOption(OptionType.BOOLEAN, "ephemeral", "Whether or not the message should be sent as an ephemeral message."),
				Commands.slash("animal", "Finds a random animal")
						.addOptions(
								new OptionData(OptionType.STRING, "type", "The type of animal to find")
										.addChoice("Bird", "bird")
										.addChoice("Big Cat", "bigcat")
										.addChoice("Canine", "canine")
										.addChoice("Fish", "fish")
						)
		).queue();

	}

	// 초기화 이후, 명령어에 따른 동작 실행
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {

		// 명령 실행
		try { CmdController.command(event); } catch (Exception e) { log.error(ExceptionUtils.getStackTrace(e)); }

	}

	// 초기화 이후, 슬래시 입력 시 동작 실행
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		log.debug("슬래시 입력받음");
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
		log.debug("슬래시 입력");
		if (event.getComponentId().equals("hello")) {
			event.reply("Hello :)").queue(); // send a message in the channel
		} else if (event.getComponentId().equals("emoji")) {
			event.editMessage("That button didn't say click me").queue(); // update the message
		}
	}

}
