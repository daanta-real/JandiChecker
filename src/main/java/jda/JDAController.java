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
	public static JDAController instance;
	public JDA jda;
	private String CMD_ME;
	private String CMD_JANDIYA;
	private String CMD_NAME;
	private String CMD_ID;
	private String CMD_LIST_YESTERDAY_SUCCEED;
	private String CMD_LIST_YESTERDAY_FAIL;
	private String CMD_LIST_TODAY_SUCCEED;
	private String CMD_LIST_BY_DATE;
	private String CMD_ABOUT;
	private String CMD_CLOSE;
    private String CMD_TRANSLATE_MAIN_TO_EN;
    private String CMD_TRANSLATE_EN_TO_MAIN;

	private JDAController() {
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Initializer
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void init() {

		instance = new JDAController();

		// Create JDA instance
		log.info("");
		log.info("[{} JDA {} {}]",
				pr.l("appName"),
				pr.l("instance"),
				pr.l("creation"));
		// Originally in the old version of JDA the try ~ catch block is needed
		// but seems gone after several patches
		instance.jda = JDABuilder.createDefault(pr.getToken_JDA())
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
		instance.jda.addEventListener(bot); // 만들어진 리스너 봇을 JdaObj의 인스턴스 내부에 할당
		log.info("[Event Listener {}: {}]",
				pr.l("creation"),
				bot);

		// Set props
		instance.CMD_ME = pr.l("me");
		instance.CMD_JANDIYA = pr.l("cmdName_heyJandi");
		instance.CMD_NAME = pr.l("info");
		instance.CMD_ID = "id";
		instance.CMD_LIST_YESTERDAY_SUCCEED = pr.l("yesterday");
		instance.CMD_LIST_YESTERDAY_FAIL = pr.l("cmdName_yesterday_fail");
		instance.CMD_LIST_TODAY_SUCCEED = pr.l("today");
		instance.CMD_LIST_BY_DATE = pr.l("date");
		instance.CMD_ABOUT = pr.l("about");
		instance.CMD_CLOSE = pr.l("close");
		instance.CMD_TRANSLATE_MAIN_TO_EN = pr.l("cmdName_MainToEN");
		instance.CMD_TRANSLATE_EN_TO_MAIN = pr.l("cmdName_ENToMain");

		// Add slash commands
		log.info("");
		log.info("[{} {} {} {}]",
				pr.l("appName"),
				pr.l("slash"),
				pr.l("command"),
				pr.l("creation"));
		List<CommandData> cmdList = SlashMenu.getMenusList();
		instance.jda.updateCommands().addCommands(cmdList).queue();
		log.info("[{} {} {} {}]",
				pr.l("slash"),
				pr.l("command"),
				pr.l("creation"),
				pr.l("fin"));

	}



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Getters
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public JDA getJda() {
		return jda;
	}

	public String getCmdMe() {
		return CMD_ME;
	}

	public String getCmdJandiya() {
		return CMD_JANDIYA;
	}

	public String getCmdName() {
		return CMD_NAME;
	}

	public String getCmdId() {
		return CMD_ID;
	}

	public String getCmdListYesterdaySucceed() {
		return CMD_LIST_YESTERDAY_SUCCEED;
	}

	public String getCmdListYesterdayFail() {
		return CMD_LIST_YESTERDAY_FAIL;
	}

	public String getCmdListTodaySucceed() {
		return CMD_LIST_TODAY_SUCCEED;
	}

	public String getCmdListByDate() {
		return CMD_LIST_BY_DATE;
	}

	public String getCmdAbout() {
		return CMD_ABOUT;
	}

	public String getCmdClose() {
		return CMD_CLOSE;
	}

	public String getCmdTranslateMainToEn() {
		return CMD_TRANSLATE_MAIN_TO_EN;
	}

	public String getCmdTranslateEnToMain() {
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
