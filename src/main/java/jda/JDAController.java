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

import java.util.List;

import static init.Initializer.pr;

@Slf4j
public class JDAController extends ListenerAdapter {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Fields
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static JDA instance;
	public static String CMD_ME;
	public static String CMD_JANDIYA;
	public static String CMD_NAME;
	public static String CMD_ID;
	public static String CMD_LIST_YESTERDAY_SUCCEED;
	public static String CMD_LIST_YESTERDAY_FAIL;
	public static String CMD_LIST_TODAY_SUCCEED;
	public static String CMD_LIST_BY_DATE;
	public static String CMD_ABOUT;
	public static String CMD_CLOSE;
    public static String CMD_TRANSLATE_MAIN_TO_EN;
    public static String CMD_TRANSLATE_EN_TO_MAIN;


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Initializer
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void init() {

		// Set props
		CMD_ME = pr.l("cmdName_me");
		CMD_JANDIYA = pr.l("cmdName_heyJandi");
		CMD_NAME = pr.l("cmdName_name");
		CMD_ID = "id";
		CMD_LIST_YESTERDAY_SUCCEED = pr.l("cmdName_yesterday_succeed");
		CMD_LIST_YESTERDAY_FAIL = pr.l("cmdName_yesterday_fail");
		CMD_LIST_TODAY_SUCCEED = pr.l("cmdName_today_succeed");
		CMD_LIST_BY_DATE = pr.l("cmdName_list_by_date");
		CMD_ABOUT = pr.l("cmdName_about");
		CMD_CLOSE = pr.l("cmdName_close");
		CMD_TRANSLATE_MAIN_TO_EN = pr.l("cmdName_MainToEN");
		CMD_TRANSLATE_EN_TO_MAIN = pr.l("cmdName_ENToMain");

		// Create JDA instance
		log.info("");
		log.info("[{} JDA {} {}]",
				pr.l("appName"),
				pr.l("instance"),
				pr.l("creation"));
		// Originally in the old version of JDA the try ~ catch block is needed
		// but seems gone after several patches
		instance = JDABuilder.createDefault(pr.getToken_JDA())
				.enableIntents(GatewayIntent.MESSAGE_CONTENT) // From JDA 4.2.0 the policy has changed so have to activate the authority yourself
				.build(); // Make a bot, make it logged in, and set as the instance of JdaObj
		log.info("[JDA {} {} {}: {}]",
				pr.l("instance"),
				pr.l("creation"),
				pr.l("fin"),
				instance);

		// Put the Event Listener bot in JDA
		log.info("");
		log.info("[{} JDA Listener {}]",
				pr.l("appName"),
				pr.l("initialization"));
		ListenerAdapter bot = new JDAController(); // JDA Bot instance. also the Listener
		instance.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		log.info("[Event Listener {}: {}]",
				pr.l("creation"),
				bot);

		// 슬래시 커맨드 추가
		log.info("");
		log.info("[{} {} {} {}]",
				pr.l("appName"),
				pr.l("slash"),
				pr.l("command"),
				pr.l("creation"));
		List<CommandData> cmdList = SlashMenu.getMenusList();
		instance.updateCommands().addCommands(cmdList).queue();
		log.info("[{} {} {} {}]",
				pr.l("slash"),
				pr.l("command"),
				pr.l("creation"),
				pr.l("fin"));

	}



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Getters
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static String getCmdMe() {
		return CMD_ME;
	}

	public static String getCmdJandiya() {
		return CMD_JANDIYA;
	}

	public static String getCmdName() {
		return CMD_NAME;
	}

	public static String getCmdId() {
		return CMD_ID;
	}

	public static String getCmdListYesterdaySucceed() {
		return CMD_LIST_YESTERDAY_SUCCEED;
	}

	public static String getCmdListYesterdayFail() {
		return CMD_LIST_YESTERDAY_FAIL;
	}

	public static String getCmdListTodaySucceed() {
		return CMD_LIST_TODAY_SUCCEED;
	}

	public static String getCmdListByDate() {
		return CMD_LIST_BY_DATE;
	}

	public static String getCmdAbout() {
		return CMD_ABOUT;
	}

	public static String getCmdClose() {
		return CMD_CLOSE;
	}

	public static String getCmdTranslateMainToEn() {
		return CMD_TRANSLATE_MAIN_TO_EN;
	}

	public static String getCmdTranslateEnToMain() {
		return CMD_TRANSLATE_EN_TO_MAIN;
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
