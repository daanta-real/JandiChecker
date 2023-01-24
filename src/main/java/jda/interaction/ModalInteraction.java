package jda.interaction;

import chat.ChatService;
import cmd.CmdService;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.apache.commons.lang3.StringUtils;
import translate.TranslationService;

import java.util.Objects;

// 모달 관련된 각종 동작(UI 생성, 표시 및 이에 대한 사용자 입력에 따른 후속 동작까지)
@Slf4j
public class ModalInteraction {

    // Get displayed name from event. If fails it'll return discord name(tagname) as alt
    private static String getDisplayedName(ModalInteractionEvent event) {
        User user = Objects.requireNonNull(event.getMember()).getUser();
        String discordTagID = user.getAsTag();
        String displayedName;
        try {
            displayedName = Initializer.getMemberNameByDiscordTagID(discordTagID);
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
                    String gitHubID = Initializer.getGitHubIDByMemberName(targetMemberName);

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
                    log.debug("그룹원 {}님의 질문: {}", name, questionMain);

                    // Compute
                    String answerMain = ChatService.getChatAnswer(questionMain);
                    log.debug("답변: {}", answerMain);
                    result = """
                        🤔 %s님의 질문... 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 ChatGPT AI님 가라사대... \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        📌 "잔디야 bla bla..." 이런 식으로 질문하시면 약간 더 긴 답변을 받을 수 있습니다.
                        ```
                        """.formatted(name, questionMain, answerMain);

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
                    log.debug("그룹원 {}님의 영한 번역 요청: {}", name, questionEng);

                    // Compute
                    String answerMain = TranslationService.translateEngToMain(questionEng);
                    log.debug("번역된 문장: {}", answerMain);
                    result = """
                        🤔 %s님의 입력.. 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 Google신이 번역한 문장.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        ```
                        """.formatted(name, questionEng, answerMain);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showTranslate_MAIN_to_EN" -> {

                    // Prepare
                    String name = getDisplayedName(event);
                    String questionMain = getOptionTextValue(event);
                    log.debug("그룹원 {}님의 한영 번역 요청: {}", name, questionMain);

                    // Compute
                    String answerEng = TranslationService.translateMainToEng(questionMain);
                    log.debug("번역된 문장: {}", answerEng);
                    result = """
                        🤔 %s님의 입력.. 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 Google신이 번역한 문장.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        ```
                        """.formatted(name, questionMain, answerEng);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                default -> result = "모듈 온 게 없는데요..";
            }

            // If the result is null or blank String then throw an AssertException
            assert !StringUtils.isEmpty(result);

        } catch(Exception e) {
            result = "정확히 입력해 주세요.";
            event.getHook().sendMessage(result).queue();
        }


    }

}
