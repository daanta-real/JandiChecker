package jda.interaction;

import cmd.CmdService;
import init.Initializer;
import jda.JDAController;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ButtonInteraction {

    public static void run(ButtonInteractionEvent event) {

        // Get command and options
        String cmd = event.getComponentId(); // Command
        log.debug("[[[ 버튼 누르기로 명령을 접수하였습니다. ]]] [{}]", cmd);

        String result = null;
        try {

            // Run each command
            switch(cmd) {
                case JDAController.CMD_ME -> {
                    event.deferEdit().queue(); // Print the loading spinner
                    result = CmdService.showJandiMapOfMe(event.getUser()); // 내 잔디정보를 출력
                    event.getMessage().delete().queue();
                }
                case JDAController.CMD_JANDIYA -> {
                    event.deferEdit().queue(); // Print the loading spinner
                    // TODO: 텍스트 입력 모달
                    //ChatService.getChatAnswerWithButton(event); // 일반적인 질문에 답하는 AI
                }
                case JDAController.CMD_NAME -> {
                    CmdService.showJandiMapByNameWithButton(event); // 특정 이름의 그룹원의 종합 잔디정보 출력
                    // TODO: 텍스트 입력 모달
                }
                case JDAController.CMD_ID -> ModalInteraction.showJandiMapById(event); // 특정 Github ID의 종합 잔디정보 출력
                case JDAController.CMD_LIST_YESTERDAY_SUCCESS -> result = CmdService.showDidCommitYesterday(); // 어제 잔디심기 한 그룹원 목록 출력
                case JDAController.CMD_LIST_YESTERDAY_FAIL -> result = CmdService.showNotCommittedYesterday(); // 어제 잔디심기 안 한 그룹원 목록 출력
                case JDAController.CMD_LIST_TODAY_SUCCESS -> result = CmdService.showDidCommitToday(); // 오늘 잔디심기 한 그룹원 목록 출력
                case JDAController.CMD_LIST_BY_DATE -> {
                    // TODO: 텍스트 입력 모달
                    CmdService.showDidCommitSomedayWithButton(event); // 특정 날짜에 잔디를 심은 그룹원 목록 출력
                }
                case JDAController.CMD_ABOUT -> result = Initializer.INFO_STRING; // 소개말
                case JDAController.CMD_CLOSE -> {
                    event.getMessage().delete().queue();
                    return;
                }
                default -> throw new Exception();
            }

        } catch(Exception e) {
            result = "정보 획득에 실패하였습니다.";
        }

        // TODO
        // Switch문의 리턴값 중에 텍스트 모달 입력 등이 있는 경우 result가 null이다.
        if(!StringUtils.isEmpty(result)) {
            event.getChannel().sendMessage(result).queue();
        } else {
            System.out.println("result가 null입니다. 메세지를 표시하지 않습니다.");
        }

    }

}
