package jda.interaction;

import chat.ChatService;
import cmd.CmdService;
import com.google.cloud.translate.TranslateException;
import jda.JDAController;
import jda.JDAMsgService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.commons.lang3.StringUtils;
import translate.TranslationService;

import java.util.Objects;

import static init.Pr.pr;

// All the execution methods about command inputs
@Slf4j
public class SlashInteraction {

    private SlashInteraction() {}

    private static String getDisplayedName(SlashCommandInteractionEvent event) {
        User user = Objects.requireNonNull(event.getMember()).getUser();
        String discordName = user.getName();
        String displayedName;
        try {
            displayedName = pr.getMemberNameByDiscordName(discordName);
        } catch(Exception e) {
            displayedName = discordName;
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
        if(StringUtils.equals(cmd, JDAController.instance.getCmdAbout())) {

            // Introduce of JandiChecker (1/2)
            result = pr.getInformation1();
            event.getHook().sendMessage(result).queue();

            // Introduce of JandiChecker (2/2)
            result = pr.getInformation2();
            event.getHook().sendMessage(result).queue();

        } else {
            try {
                if(StringUtils.equals(cmd, JDAController.instance.getCmdMe())) {
                    result = CmdService.getJandiMapStringOfMine(event.getUser()); // Get my Commit map info
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdHeyJandi())) {
                    result = makeChatAnswer(event, option); // The ChatGPT answers for general questions
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdName())) {
                    result = CmdService.getJandiMapStringByName(option); // Show the total commit info of the member by the specific name
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdId())) {
                    result = CmdService.getJandiMapStringById(option); // Show the total commit info of the member by the GitHub ID
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdListYesterdaySucceed())) {
                    result = CmdService.getDidCommitStringYesterday(); // Show the member list succeed to commit yesterday
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdListYesterdayFailed())) {
                    result = CmdService.getNotCommittedStringYesterday(); // Show the member list failed to commit yesterday
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdListTodaySucceed())) {
                    result = CmdService.getDidCommitStringToday(); // Show the member list succeed to commit today
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdListByDate())) {
                    result = CmdService.getDidCommitStringSomeday(option); // Show the member list succeed to commit in specific day
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdTranslateEnToMain())) {
                    result = getTranslatedString_EN_to_MAIN(event, option); // English → Main language translation
                } else if(StringUtils.equals(cmd, JDAController.instance.getCmdTranslateMainToEn())) {
                    result = getTranslatedString_MAIN_to_EN(event, option); // Main language → English translation
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

        try {
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
        } catch(TranslateException e) {
            log.debug("\n==========\n\n");
            log.debug("TRANSLATE ERROR: {}", e.getReason());
            log.debug(e.getMessage());
            log.debug("\n\n==========\n");
            return pr.l("err_fromAPI");
        } catch(Exception e) {
            log.debug("Etc error has occured.");
            return pr.l("err_fromAPI");
        }

    }

    private static String getTranslatedString_MAIN_to_EN(SlashCommandInteractionEvent event, String questionMain) {

        try {
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
        } catch(TranslateException e) {
            log.debug("\n==========\n\n");
            log.debug("TRANSLATE ERROR: {}", e.getReason());
            log.debug(e.getMessage());
            log.debug("\n\n==========\n");
            return pr.l("err_fromAPI");
        } catch(Exception e) {
            log.debug("Etc error has occured.");
            return pr.l("err_fromAPI");
        }

    }

}
