package jda.interaction;

import chat.ChatService;
import cmd.CmdService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.apache.commons.lang3.StringUtils;
import translate.TranslationService;

import java.util.Objects;

import static init.Pr.pr;

// All the execution methods about modal menus(making and showing UI, getting users' input, interactions after it..)
@Slf4j
public class ModalInteraction {

    // Get displayed name from event. If fails it'll return discord name(tagname) as alt
    private static String getDisplayedName(ModalInteractionEvent event) {
        User user = Objects.requireNonNull(event.getMember()).getUser();
        String discordTagID = user.getAsTag();
        String displayedName;
        try {
            displayedName = pr.getMemberNameByDiscordTagID(discordTagID);
        } catch(Exception e) {
            displayedName = discordTagID;
        }
        return displayedName;
    }

    // Get option text value. Only available for the text box's id named "value"
    private static String getOptionTextValue(ModalInteractionEvent event) {
        ModalMapping m = event.getValue("option");
        assert m != null;
        return m.getAsString();
    }

    // Main interaction
    public static void run(ModalInteractionEvent event) {

        event.deferReply().queue();

        String result;
        try {
            switch (event.getModalId()) {
                case "showJandiMapByName" -> {

                    // Prepare
                    String targetMemberName = getOptionTextValue(event);
                    String gitHubID = pr.getGitHubIDByMemberName(targetMemberName);

                    // Compute
                    result = CmdService.getJandiMapStringByIdAndName(targetMemberName, gitHubID);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showJandiMapById" -> {

                    // Prepare
                    String id = getOptionTextValue(event);

                    // Compute
                    result = CmdService.getJandiMapStringById(id);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "getChatAnswer" -> {

                    // Prepare
                    String name = getDisplayedName(event);
                    String questionMain = getOptionTextValue(event);
                    log.debug(pr.l("chat_theQueryByName"), name, questionMain);

                    // Compute
                    String answerMain = ChatService.getChatAnswer(questionMain);
                    log.debug(pr.l("chat_theAnswer"), answerMain);
                    String hisQuestion = pr.l("chat_questionByName").formatted(name);
                    result = """
                        🤔 %s.. 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        📌 %s
                        ```
                        """.formatted(hisQuestion, questionMain, pr.l("chat_GPTSays"),
                            answerMain, pr.l("tip_howToGetLongAnswer"));

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showDidCommitSomeday" -> {

                    // Prepare
                    String date = getOptionTextValue(event);

                    // Compute
                    result = CmdService.getDidCommitStringSomeday(date);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                // 2 translation cmds below are available only in non-English mode
                case "showTranslate_EN_to_MAIN" -> {

                    // Prepare
                    String name = getDisplayedName(event);
                    String questionEng = getOptionTextValue(event);
                    log.debug("Translation request(English → {}) by {}: {}",
                            TranslationService.mainLanguageLong, name, questionEng);

                    // Compute
                    String answerMain = TranslationService.translateEngToMain(questionEng);
                    log.debug("Translated text to {}: {}", TranslationService.mainLanguageLong, answerMain);
                    String inputByName = pr.l("transl_inputByName").formatted(name);

                    result = """
                        🤔 %s.. 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        ```
                        """.formatted(inputByName, questionEng, pr.l("transl_textByGoogle"), answerMain);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showTranslate_MAIN_to_EN" -> {

                    // Prepare
                    String name = getDisplayedName(event);
                    String questionMain = getOptionTextValue(event);
                    log.debug("Translation request({} → English) by {}: {}",
                            TranslationService.mainLanguageLong, name, questionMain);

                    // Compute
                    String answerEng = TranslationService.translateMainToEng(questionMain);
                    log.debug("Translated text to English: {}", answerEng);
                    String inputByName = pr.l("transl_inputByName").formatted(name);

                    result = """
                        🤔 %s.. 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        ```
                        """.formatted(inputByName, questionMain, pr.l("transl_textByGoogle"), answerEng);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                default -> result = pr.l("err_failedToGetInfo");
            }

            // If the result is null or blank String then throw an AssertException
            assert !StringUtils.isEmpty(result);

        } catch(Exception e) {
            result = pr.l("err_incorrectInput");
            event.getHook().sendMessage(result).queue();
        }


    }

}
