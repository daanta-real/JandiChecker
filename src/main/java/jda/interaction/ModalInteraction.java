package jda.interaction;

import cmd.CmdService;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.apache.commons.lang3.StringUtils;

// 모달 관련된 각종 동작(UI 생성, 표시 및 이에 대한 사용자 입력에 따른 후속 동작까지)
public class ModalInteraction {

    // Main interaction
    public static void run(ModalInteractionEvent event) {

        String result = null;
        try {
            String modalId = event.getModalId();
            switch (modalId) {
                case "showJandiMapById" -> {
                    String id = event.getValue("showJandiMapByIdText").getAsString();
                    result = CmdService.showJandiMapById(id);
                }
                default -> {
                    result = "모듈 온 게 없는데요..";
                }
            }

            // If the result is null or blank String then throw an AssertException
            assert !StringUtils.isEmpty(result);

        } catch(Exception e) {
            result = "정확히 입력해 주세요.";
        }

        event.getChannel().sendMessage(result).queue();

    }

    // 특정 ID의 종합 잔디정보를 리턴
    public static String showJandiMapById(ButtonInteractionEvent event) {

        // TODO
        TextInput id = TextInput.create("showJandiMapByIdText", "인자라벨", TextInputStyle.SHORT)
                .setMinLength(1)
                .setMaxLength(20)
                .setLabel("안쪽라벨")
                .setPlaceholder("조회하고자 하는 아이디를 입력하세요. (최대 20글자)")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("showJandiMapById", "아이디아이디")
                .addActionRows(ActionRow.of(id))
                .build();

        event.replyModal(modal).queue();

        return null;

    }

}
