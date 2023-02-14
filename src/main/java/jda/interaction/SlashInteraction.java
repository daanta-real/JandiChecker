package jda.interaction;

import chat.ChatService;
import cmd.CmdService;
import jda.JDAController;
import jda.JDAMsgService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.commons.lang3.StringUtils;
import translate.TranslationService;

import java.util.Objects;

import static init.Initializer.pr;

// All the execution methods about command inputs
@Slf4j
public class SlashInteraction {

    private static String getDisplayedName(SlashCommandInteractionEvent event) {
        User user = Objects.requireNonNull(event.getMember()).getUser();
        String discordTag = user.getAsTag();
        String displayedName;
        try {
            displayedName = pr.getMemberNameByDiscordTagID(discordTag);
        } catch(Exception e) {
            displayedName = discordTag;
        }
        return displayedName;
    }

    public static void run(SlashCommandInteractionEvent event) {

        // Print the loading spinner
        event.deferReply().queue();

        // Get command and options
        String cmd = event.getName(); // Command
        OptionMapping o = event.getOption("option");
        String option = o != null ? o.getAsString() : ""; // Option string
        log.debug("[[[ COMMAND RECEIVED BY SLASH INPUT ]]]: [{}], [{}]", cmd, option);

        // Make result
        String result;
        try {
            if(StringUtils.equals(cmd, JDAController.instance.getCmdMe())) {
                result = CmdService.getJandiMapStringOfMine(event.getUser()); // Get my Commit map info
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdJandiya())) {
                result = makeChatAnswer(event, option); // The ChatGPT answers for general questions
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdName())) {
                result = CmdService.getJandiMapStringByName(option); // Show the total commit info of the member by the specific name
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdId())) {
                result = CmdService.getJandiMapStringById(option); // Show the total commit info of the member by the GitHub ID
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdListYesterdayFail())) {
                result = CmdService.getDidCommitStringYesterday(); // Show the member list succeed to commit yesterday
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdListYesterdayFail())) {
                result = CmdService.getNotCommittedStringYesterday(); // Show the member list failed to commit yesterday
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdListTodaySucceed())) {
                result = CmdService.getDidCommitStringToday(); // Show the member list succeed to commit today
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdListByDate())) {
                result = CmdService.getDidCommitStringSomeday(option); // Show the member list succeed to commit in specific day
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdTranslateEnToMain())) {
                result = getTranslatedString_EN_to_MAIN(event, option); // English → Main language translation
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdTranslateMainToEn())) {
                result = getTranslatedString_MAIN_to_EN(event, option); // Main language → English translation
            } else if(StringUtils.equals(cmd, JDAController.instance.getCmdAbout())) {
                result = pr.getInformation(); // Introduce of JandiChecker
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            result = pr.l("err_failedToGetInfo");
        }

        // Send
        result = JDAMsgService.msgTrim(result);
        event.getHook().sendMessage(result).queue();

    }

    private static String makeChatAnswer(SlashCommandInteractionEvent event, String questionMain) {

        String name = getDisplayedName(event);
        String answerMain = ChatService.getChatAnswer(questionMain);
        String chatQuestionByName = pr.l("chat_questionByName").formatted(name);

        return """
                🤔 %s... 🤔```md
                %s
                ```
                \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s... \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                ```
                %s
                
                📌 %s
                ```
                """.formatted(chatQuestionByName, questionMain, pr.l("chat_GPTSays"),
                answerMain, pr.l("tip_howToGetLongAnswer"));

    }

    private static String getTranslatedString_EN_to_MAIN(SlashCommandInteractionEvent event, String questionMain) {

        String name = getDisplayedName(event);
        String answerMain = TranslationService.translateEngToMain(questionMain);
        String inputByName = pr.l("transl_inputByName").formatted(name);

        return """
                🤔 %s.. 🤔```md
                %s
                ```
                \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                ```
                %s
                ```
                """.formatted(inputByName, questionMain, pr.l("transl_textByGoogle"), answerMain);

    }

    private static String getTranslatedString_MAIN_to_EN(SlashCommandInteractionEvent event, String questionMain) {

        String name = getDisplayedName(event);
        String answerMain = TranslationService.translateMainToEng(questionMain);
        String inputByName = pr.l("transl_inputByName").formatted(name);

        return """
                🤔 %s.. 🤔```md
                %s
                ```
                \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                ```
                %s
                ```
                """.formatted(inputByName, questionMain, pr.l("transl_textByGoogle"), answerMain);

    }

}
