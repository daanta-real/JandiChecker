package jda.interaction;

import chat.ChatService;
import cmd.CmdService;
import init.Initializer;
import jda.JDAController;
import jda.JDAMsgSender;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

// 슬래시 메뉴판을 사용한 커맨드 입력에 따른 동작 실행
@Slf4j
public class SlashInteraction {

    public static void run(SlashCommandInteractionEvent event) {

        // Print the loading spinner
        event.deferReply().queue();

        // Get command and options
        String cmd = event.getName(); // Command
        OptionMapping o = event.getOption("option");
        String option = o != null ? o.getAsString() : ""; // Option string
        log.debug("[[[ 슬래시 명령문 입력으로 명령을 접수하였습니다. ]]] [{}: '{}']", cmd, option);

        // Make result
        String result;
        try {
            result = switch (cmd) {
                case JDAController.CMD_ME                     -> CmdService.getJandiMapStringOfMe(event.getUser()); // 내 잔디정보를 출력
                case JDAController.CMD_JANDIYA                -> makeChatAnswer(event, option); // 일반적인 질문에 답하는 AI
                case JDAController.CMD_NAME                   -> CmdService.getJandiMapStringByName(option); // 특정 이름의 그룹원의 종합 잔디정보 출력
                case JDAController.CMD_ID                     -> CmdService.getJandiMapStringByById(option); // 특정 Github ID의 종합 잔디정보 출력
                case JDAController.CMD_LIST_YESTERDAY_SUCCESS -> CmdService.getDidCommitStringYesterday(); // 어제 잔디심기 한 그룹원 목록 출력
                case JDAController.CMD_LIST_YESTERDAY_FAIL    -> CmdService.getNotCommittedStringYesterday(); // 어제 잔디심기 안 한 그룹원 목록 출력
                case JDAController.CMD_LIST_TODAY_SUCCESS     -> CmdService.getDidCommitStringToday(); // 오늘 잔디심기 한 그룹원 목록 출력
                case JDAController.CMD_LIST_BY_DATE           -> CmdService.getDidCommitStringSomeday(option); // 특정 날짜에 잔디를 심은 그룹원 목록 출력
                case JDAController.CMD_TRANSLATE_KR_TO_EN     -> CmdService.getTranslatedString_KR_to_EN(option); // 한영번역
                case JDAController.CMD_TRANSLATE_EN_TO_KR     -> CmdService.getTranslatedString_EN_to_KR(option); // 영한번역
                case JDAController.CMD_ABOUT                  -> Initializer.INFO_STRING; // 소개말
                default -> throw new Exception();
            };
        } catch (Exception e) {
            result = "정보 획득에 실패하였습니다.";
        }

        // Send
        result = JDAMsgSender.msgTrim(result);
        event.getHook().sendMessage(result).queue();

    }

    private static String makeChatAnswer(SlashCommandInteractionEvent event, String questionKor) {

        User user = Objects.requireNonNull(event.getMember()).getUser();
        String name = user.getName();
        if(StringUtils.isEmpty(name)) return "질문자의 ID가 명확하지 않습니다.";

        String answerKor = ChatService.getChatAnswer(questionKor);

        return """
                        🤔 %s님의 질문... 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 ChatGPT AI님 가라사대... \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                                            
                        📌 "잔디야 bla bla..." 이런 식으로 질문하시면 약간 더 긴 답변을 받을 수 있습니다.
                        ```
                        """.formatted(name, questionKor, answerKor);

    }

}
