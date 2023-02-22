package jda.menu;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import static init.Pr.pr;

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

        event.getMessage().delete().queue(); // Remove all the menu and the original messages

        System.out.println("id: {}" + id);
        System.out.println("mainTitle: {}" + mainTitle);
        System.out.println("labelTitle: {}" + labelTitle);
        System.out.println("valuePlaceholder: {}" + valuePlaceholder);
        System.out.println("minLength: {}" + minLength);
        System.out.println("maxLength: {}" + maxLength);
        System.out.println("isParagraph: {}" + isParagraph);

        TextInput name = TextInput.create("option", pr.l("appName"), style)
                .setMinLength(minLength)
                .setMaxLength(maxLength)
                .setLabel(labelTitle)
                .setPlaceholder(valuePlaceholder)
                .setRequired(true)
                .build();

        Modal modal = Modal.create(id , mainTitle)
                .addComponents(ActionRow.of(name))
                .build();

        event.replyModal(modal).queue();

    }

    // Show the total commit info of the member by the specific name
    public static void showJandiMapByName(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showJandiMapByName",
                pr.l("modal_inputMemberName"),
                pr.l("modal_inputMemberName_description"),
                pr.l("modal_maxLen10"),
                1, 10);
    }

    // Show the total commit info of the member by the GitHub ID
    public static void showJandiMapById(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showJandiMapById",
                pr.l("modal_inputGitHubID"),
                "GitHub ID", pr.l("modal_maxLen20"),
                1, 20);
    }

    // The ChatGPT answers for general questions
    public static void getChatAnswer(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "getChatAnswer",
                pr.l("modal_inputTheQuestion"),
                pr.l("modal_inputTheQuestion_description"),
                pr.l("modal_inputTheQuestion_placeholder"),
                1, 300, true);
    }

    // Show the member list succeed to commit in specific day
    public static void showDidCommitSomeday(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showDidCommitSomeday",
                pr.l("modal_inputTheDate"),
                pr.l("modal_inputTheDate_description"),
                pr.l("modal_inputTheDate_placeholder"),
                8, 8);
    }

    // English → Main language translation
    public static void showTranslate_EN_to_MAIN(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showTranslate_EN_to_MAIN",
                pr.l("modal_inputTheEnglish"),
                pr.l("modal_inputTheEnglish_description"),
                pr.l("modal_maybeLongerBeOk"),
                1, 300, true);
    }

    // Main language → English translation
    public static void showTranslate_MAIN_to_EN(ButtonInteractionEvent event) {
        sendSingleTextInput(event, "showTranslate_MAIN_to_EN",
                pr.l("modal_inputTheMainLang"),
                pr.l("modal_inputTheMainLang_description"),
                pr.l("modal_maybeLongerBeOk"),
                1, 300, true);
    }

}
