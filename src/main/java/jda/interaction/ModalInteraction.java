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

    // Main interaction
    public static void run(ModalInteractionEvent event) {

        event.deferReply().queue();

        String result;
        try {
            switch (event.getModalId()) {
                case "showJandiMapByName" -> {

                    // Prepare
                    ModalMapping m = event.getValue("showJandiMapByNameText");
                    assert m != null;
                    String targetMemberName = m.getAsString();
                    String gitHubID = Initializer.getGitHubIDByMemberName(targetMemberName);

                    // Compute
                    result = CmdService.getJandiMapStringByIdAndName(targetMemberName, gitHubID);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showJandiMapById" -> {

                    // Prepare
                    ModalMapping m = event.getValue("showJandiMapByIdText");
                    assert m != null;
                    String id = m.getAsString();

                    // Compute
                    result = CmdService.getJandiMapStringByById(id);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "getChatAnswer" -> {

                    // Prepare
                    User user = Objects.requireNonNull(event.getMember()).getUser();
                    String discordID = user.getAsTag();
                    String memberName = Initializer.getMemberNameByDiscordID(discordID);
                    ModalMapping m = event.getValue("getChatAnswerText");
                    assert m != null;
                    String questionKor = m.getAsString();
                    log.debug("그룹원 {}님의 질문: {}", memberName, questionKor);

                    // Compute
                    String answerKor = ChatService.getChatAnswer(questionKor);
                    log.debug("답변: {}", answerKor);
                    result = """
                        🤔 %s님의 질문... 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 ChatGPT AI님 가라사대... \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        📌 "잔디야 bla bla..." 이런 식으로 질문하시면 약간 더 긴 답변을 받을 수 있습니다.
                        ```
                        """.formatted(memberName, questionKor, answerKor);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showDidCommitSomeday" -> {

                    // Prepare
                    ModalMapping m = event.getValue("showDidCommitSomedayText");
                    assert m != null;
                    String date = m.getAsString();

                    // Compute
                    result = CmdService.getDidCommitStringSomeday(date);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showTranslate_EN_to_KR" -> {

                    // Prepare
                    User user = Objects.requireNonNull(event.getMember()).getUser();
                    String discordID = user.getAsTag();
                    String memberName = Initializer.getMemberNameByDiscordID(discordID);
                    ModalMapping m = event.getValue("showTranslate_EN_to_KRText");
                    assert m != null;
                    String questionEng = m.getAsString();
                    log.debug("그룹원 {}님의 영한 번역 요청: {}", memberName, questionEng);

                    // Compute
                    String answerKor = TranslationService.translateEngToKor(questionEng);
                    log.debug("번역된 문장: {}", answerKor);
                    result = """
                        🤔 %s님의 입력.. 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 Google신이 번역한 문장.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        ```
                        """.formatted(memberName, questionEng, answerKor);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showTranslate_KR_to_EN" -> {

                    // Prepare
                    User user = Objects.requireNonNull(event.getMember()).getUser();
                    String discordID = user.getAsTag();
                    String memberName = Initializer.getMemberNameByDiscordID(discordID);
                    ModalMapping m = event.getValue("showTranslate_KR_to_ENText");
                    assert m != null;
                    String questionKor = m.getAsString();
                    log.debug("그룹원 {}님의 한영 번역 요청: {}", memberName, questionKor);

                    // Compute
                    String answereng = TranslationService.translateKorToEng(questionKor);
                    log.debug("번역된 문장: {}", answereng);
                    result = """
                        🤔 %s님의 입력.. 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 Google신이 번역한 문장.. \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                        ```
                        """.formatted(memberName, questionKor, answereng);

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
