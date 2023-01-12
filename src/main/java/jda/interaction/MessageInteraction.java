package jda.interaction;

import chat.ChatService;
import cmd.ButtonMenues;
import jda.JDAMsgSender;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

// 슬래시를 사용하지 않은 raw 메세지 발생에 따른 동작 실행
public class MessageInteraction {

    public static void run(MessageReceivedEvent event) {

        // 메세지 입수
        String message = event.getMessage().getContentRaw();

        // 접수된 메세지가 비었을 경우 리턴.
        if(message.isBlank()) return;

        // 명령어에 따른 분기
        // 1. "잔디야" 라고만 치면 메뉴판 호출 후 리턴
        if(message.equals("잔디야")) {
            ButtonMenues.showButtonMenues(event);
        }
        // 2. "잔디야 뭐뭐머뭐..." 이런 식으로 치면 뭐뭐머뭐... ← 이 부분이 질문이므로 AI의 답변을 회신
        else if(message.startsWith("잔디야 ")) {
            String question = message.substring(4);
            JDAMsgSender.send(event, ChatService.getChatAnswerByMsgCmd(question)); // 메세지를 바로 돌려준다
        }

    }

}
