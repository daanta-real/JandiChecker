package jda.menu;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class ModalMenu {

    // 특정 그룹원명의 종합 잔디정보를 리턴
    public static void showJandiMapByName(ButtonInteractionEvent event) {

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput name = TextInput.create("showJandiMapByNameText", "잔디체커", TextInputStyle.SHORT)
                .setMinLength(1)
                .setMaxLength(10)
                .setLabel("그룹원 이름")
                .setPlaceholder("최대 10글자")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("showJandiMapByName", "그룹원명 입력")
                .addActionRows(ActionRow.of(name))
                .build();

        event.replyModal(modal).queue();

    }

    // 특정 ID의 종합 잔디정보를 리턴
    public static void showJandiMapById(ButtonInteractionEvent event) {

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput id = TextInput.create("showJandiMapByIdText", "잔디체커", TextInputStyle.SHORT)
                .setMinLength(1)
                .setMaxLength(20)
                .setLabel("GitHub ID")
                .setPlaceholder("최대 20글자")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("showJandiMapById", "아이디 입력")
                .addActionRows(ActionRow.of(id))
                .build();

        event.replyModal(modal).queue();

    }

    // ChatGPT를 불러 답변을 받아 리턴
    public static void getChatAnswer(ButtonInteractionEvent event) {

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput id = TextInput.create("getChatAnswerText", "잔디체커", TextInputStyle.PARAGRAPH)
                .setMinLength(1)
                .setMaxLength(200)
                .setLabel("하고 싶은 질문")
                .setPlaceholder("최대 300글자. 글자수 제한 없이 하려면 이 버튼 메뉴를 통하지 말고 채팅으로 직접 \"잔디야 [질문내용]\"이라고 타이핑해 주세요.")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("getChatAnswer", "질문 입력")
                .addActionRows(ActionRow.of(id))
                .build();

        event.replyModal(modal).queue();

    }

    // 특정 날짜에 잔디를 심은 그룹원 목록 출력
    public static void showDidCommitSomeday(ButtonInteractionEvent event) {

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput date = TextInput.create("showDidCommitSomedayText", "잔디체커", TextInputStyle.SHORT)
                .setMinLength(8)
                .setMaxLength(8)
                .setLabel("조회할 날짜")
                .setPlaceholder("yyyyMMdd 형식")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("showDidCommitSomeday", "날짜 입력")
                .addActionRows(ActionRow.of(date))
                .build();

        event.replyModal(modal).queue();

    }

}
