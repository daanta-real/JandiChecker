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
                    // Interaction을 보내준 객체를 Spinner로 바꾼다.
                    // 여기에서 deferReply()로 하면 답변 시 기본 메뉴판이 치워지지 않으므로, deferEdit()를 사용해야 한다.
                    event.deferEdit().queue();
                    result = CmdService.showJandiMapOfMe(event.getUser()); // 내 잔디정보를 획득
                }
                case JDAController.CMD_JANDIYA -> ModalInteraction.getChatAnswer(event); // 일반적인 질문에 답하는 AI
                case JDAController.CMD_NAME -> ModalInteraction.showJandiMapByName(event); // 특정 이름의 그룹원의 종합 잔디정보 출력
                case JDAController.CMD_ID -> ModalInteraction.showJandiMapById(event); // 특정 Github ID의 종합 잔디정보 출력
                case JDAController.CMD_LIST_YESTERDAY_SUCCESS -> {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.showDidCommitYesterday(); // 어제 잔디심기 한 그룹원 목록 출력
                }
                case JDAController.CMD_LIST_YESTERDAY_FAIL -> {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.showNotCommittedYesterday(); // 어제 잔디심기 안 한 그룹원 목록 출력
                }
                case JDAController.CMD_LIST_TODAY_SUCCESS -> {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.showDidCommitToday(); // 오늘 잔디심기 한 그룹원 목록 출력
                }
                case JDAController.CMD_LIST_BY_DATE -> ModalInteraction.showDidCommitSomeday(event); // 특정 날짜에 잔디를 심은 그룹원 목록 출력
                case JDAController.CMD_ABOUT -> {
                    event.deferEdit().queue(); // Set defer
                    result = Initializer.INFO_STRING; // 소개말
                }
                case JDAController.CMD_CLOSE -> {
                    event.getMessage().delete().queue();
                    return;
                }
                default -> throw new Exception();
            }

        } catch(Exception e) {
            result = "정보 획득에 실패하였습니다.";
        }

        // Switch case문 중 모달 입력 등이 있다면, result가 초기값인 null이므로, 여기를 실행하지 않는다.
        if(!StringUtils.isEmpty(result)) {

            // Show result
            // event.reply(result).queue();
            event.getChannel().sendMessage(result).queue();

            // Remove defer message and its button menu panel
            // event.getHook().sendMessage(result).queue();
            // event.getHook().editOriginal(result).queue();
            event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        }

    }

}
