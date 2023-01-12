package jda.interaction;

import chat.ChatService;
import cmd.CmdService;
import init.Initializer;
import jda.JDAController;
import jda.JDAMsgSender;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

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
                case JDAController.CMD_ME                     -> CmdService.showJandiMapOfMeWithSlash(event); // 내 잔디정보를 출력
                case JDAController.CMD_JANDIYA                -> ChatService.getChatAnswerWithSlash(event, option); // 일반적인 질문에 답하는 AI
                case JDAController.CMD_NAME                   -> CmdService.showJandiMapByName(option); // 특정 이름의 그룹원의 종합 잔디정보 출력
                case JDAController.CMD_ID                     -> CmdService.showJandiMapById(option); // 특정 Github ID의 종합 잔디정보 출력
                case JDAController.CMD_LIST_YESTERDAY_SUCCESS -> CmdService.showDidCommitYesterday(); // 어제 잔디심기 한 그룹원 목록 출력
                case JDAController.CMD_LIST_YESTERDAY_FAIL    -> CmdService.showNotCommittedYesterday(); // 어제 잔디심기 안 한 그룹원 목록 출력
                case JDAController.CMD_LIST_TODAY_SUCCESS     -> CmdService.showDidCommitToday(); // 오늘 잔디심기 한 그룹원 목록 출력
                case JDAController.CMD_LIST_BY_DATE           -> CmdService.showDidCommitSomeday(option); // 특정 날짜에 잔디를 심은 그룹원 목록 출력
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

}
