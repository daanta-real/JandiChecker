package jda.menu;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class ModalMenu {

    private static void sendSingleTextInput(
            ButtonInteractionEvent event, String id,
            String mainTitle, String labelTitle, String valuePlaceholder,
            int minLength, int maxLength) {
        sendSingleTextInput(event, id, mainTitle, labelTitle, valuePlaceholder, minLength, maxLength, false);
    }
    private static void sendSingleTextInput(
            ButtonInteractionEvent event, String id,
            String mainTitle, String labelTitle, String valuePlaceholder,
            int minLength, int maxLength, boolean isParagraph) {

        TextInputStyle style = isParagraph ? TextInputStyle.PARAGRAPH : TextInputStyle.SHORT;

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput name = TextInput.create("option", "잔디체커", style)
                .setMinLength(minLength)
                .setMaxLength(maxLength)
                .setLabel(labelTitle)
                .setPlaceholder(valuePlaceholder)
                .setRequired(true)
                .build();

        Modal modal = Modal.create(id , mainTitle)
                .addActionRows(ActionRow.of(name))
                .build();

        event.replyModal(modal).queue();

    }

    // 특정 그룹원명의 종합 잔디정보를 리턴
    public static void showJandiMapByName(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showJandiMapByName", "그룹원명 입력", "그룹원 이름", "최대 10글자.", 1, 10);
    }

    // 특정 ID의 종합 잔디정보를 리턴
    public static void showJandiMapById(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showJandiMapById", "아이디 입력", "GitHub ID", "최대 20글자", 1, 20);
    }

    // ChatGPT를 불러 답변을 받아 리턴
    public static void getChatAnswer(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "getChatAnswer", "질문 입력", "하고 싶은 질문",
                "최대 300글자. 글자수 제한 없이 하려면 이 버튼 메뉴를 통하지 말고 채팅으로 직접 \"잔디야 [질문내용]\"이라고 타이핑해 주세요.", 1, 300, true);
    }

    // 특정 날짜에 잔디를 심은 그룹원 목록 출력
    public static void showDidCommitSomeday(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showDidCommitSomeday", "날짜 입력", "조회할 날짜","yyyyMMdd 형식", 8, 8);
    }

    // Translation: English to main language
    public static void showTranslate_EN_to_MAIN(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showTranslate_EN_to_MAIN", "한국어로 번역할 내용 입력", "영어 문장","조금 길어도 괜찮을지도..?", 1, 300, true);
    }

    // Translation: Main language to English
    public static void showTranslate_MAIN_to_EN(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showTranslate_MAIN_to_EN", "영어로 번역할 내용 입력", "한국 문장","조금 길어도 괜찮을지도..?", 1, 300, true);
    }

}
