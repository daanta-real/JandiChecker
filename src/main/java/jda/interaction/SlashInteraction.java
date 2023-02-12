package jda.interaction;

import chat.ChatService;
import cmd.CmdService;
import jda.JDAController;
import jda.JDAMsgService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import translate.TranslationService;

import java.util.Objects;

import static init.Initializer.props;

// All the execution methods about command inputs
@Slf4j
public class SlashInteraction {

    private static String getDisplayedName(SlashCommandInteractionEvent event) {
        User user = Objects.requireNonNull(event.getMember()).getUser();
        String discordTag = user.getAsTag();
        String displayedName;
        try {
            displayedName = props.getMemberNameByDiscordTagID(discordTag);
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
        log.debug("{} [{}: '{}']", props.lang("si_receivedTheCommandBySlashCommand"), cmd, option);

        // Make result
        String result;
        try {
            result = switch (cmd) {
                case JDAController.CMD_ME                     -> CmdService.getJandiMapStringOfMine(event.getUser()); // Get my Commit map info
                case JDAController.CMD_JANDIYA                -> makeChatAnswer(event, option); // The AI answers for general questions
                case JDAController.CMD_NAME                   -> CmdService.getJandiMapStringByName(option); // Show the total commit info of the member by the specific name
                case JDAController.CMD_ID                     -> CmdService.getJandiMapStringById(option); // Show the total commit info of the member by the GitHub ID
                case JDAController.CMD_LIST_YESTERDAY_SUCCEED -> CmdService.getDidCommitStringYesterday(); // Show the member list succeed to commit yesterday
                case JDAController.CMD_LIST_YESTERDAY_FAIL    -> CmdService.getNotCommittedStringYesterday(); // Show the member list failed to commit yesterday
                case JDAController.CMD_LIST_TODAY_SUCCEED -> CmdService.getDidCommitStringToday(); // Show the member list succeed to commit today
                case JDAController.CMD_LIST_BY_DATE           -> CmdService.getDidCommitStringSomeday(option); // Show the member list succeed to commit in specific day
                case JDAController.CMD_TRANSLATE_EN_TO_MAIN -> getTranslatedString_EN_to_MAIN(event, option); // English → Main language translation
                case JDAController.CMD_TRANSLATE_MAIN_TO_EN -> getTranslatedString_MAIN_to_EN(event, option); // Main language → English translation
                case JDAController.CMD_ABOUT                  -> props.getInformation(); // Introduce of JandiChecker
                default -> throw new Exception();
            };
        } catch (Exception e) {
            result = props.lang("err_failedToGetInfo");
        }

        // Send
        result = JDAMsgService.msgTrim(result);
        event.getHook().sendMessage(result).queue();

    }

    private static String makeChatAnswer(SlashCommandInteractionEvent event, String questionMain) {

        String name = getDisplayedName(event);
        String answerMain = ChatService.getChatAnswer(questionMain);
        String chatQuestionByName = props.lang("chat_questionByName").formatted(name);

        return """
                🤔 %s... 🤔```md
                %s
                ```
                \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s... \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                ```
                %s
                
                📌 %s
                ```
                """.formatted(chatQuestionByName, questionMain, props.lang("chat_GPTSays"),
                answerMain, props.lang("tip_howToGetLongAnswer"));

    }

    private static String getTranslatedString_EN_to_MAIN(SlashCommandInteractionEvent event, String questionMain) {

        String name = getDisplayedName(event);
        String answerMain = TranslationService.translateEngToMain(questionMain);
        String inputByName = props.lang("transl_inputByName").formatted(name);

        return """
                🤔 %s.. 🤔```md
                %s
                ```
                \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                ```
                %s
                ```
                """.formatted(inputByName, questionMain, props.lang("transl_textByGoogle"), answerMain);

    }

    private static String getTranslatedString_MAIN_to_EN(SlashCommandInteractionEvent event, String questionMain) {

        String name = getDisplayedName(event);
        String answerMain = TranslationService.translateMainToEng(questionMain);
        String inputByName = props.lang("transl_inputByName").formatted(name);

        return """
                🤔 %s.. 🤔```md
                %s
                ```
                \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                ```
                %s
                ```
                """.formatted(inputByName, questionMain, props.lang("transl_textByGoogle"), answerMain);

    }

}
