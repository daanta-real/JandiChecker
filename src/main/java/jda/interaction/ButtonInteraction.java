package jda.interaction;

import cmd.CmdService;
import jda.JDAController;
import jda.menu.ModalMenu;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.lang3.StringUtils;

import static init.Initializer.pr;

@Slf4j
public class ButtonInteraction {

    public static void run(ButtonInteractionEvent event) {

        // Get command and options
        String cmd = event.getComponentId(); // Command
        log.debug(pr.l("bi_receivedTheCommandByClickingButton"), cmd);

        String result = null;
        try {

            // Run each command
            switch(cmd) {
                case JDAController.getCmdMe() -> {
                    // Convert the instant got from Interaction to a spinner
                    // I guess I should use .deferEdit() not .deferReply().
                    // Because if I use .deferReply() the answer will be got as reply not the additional message.
                    // After that the reply says "The message has deleted" attached bottom of the original message,
                    // and the spinner cannot be deleted with no other ways.
                    event.deferEdit().queue();
                    result = CmdService.getJandiMapStringOfMine(event.getUser()); // Get my Commit map info
                }
                case JDAController.CMD_JANDIYA                -> ModalMenu.getChatAnswer(event); // The AI answers for general questions
                case JDAController.CMD_LIST_YESTERDAY_SUCCEED -> {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.getDidCommitStringYesterday(); // Show the member list succeed to commit yesterday
                }
                case JDAController.CMD_LIST_TODAY_SUCCEED     -> {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.getDidCommitStringToday(); // Show the member list succeed to commit today
                }
                case JDAController.CMD_NAME -> ModalMenu.showJandiMapByName(event); // Show the total commit info of the member by the specific name
                case JDAController.CMD_ID -> ModalMenu.showJandiMapById(event); // Show the total commit info of the member by the GitHub ID
                case JDAController.CMD_LIST_YESTERDAY_FAIL    -> {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.getNotCommittedStringYesterday(); // Show the member list failed to commit yesterday
                }
                case JDAController.CMD_LIST_BY_DATE           -> ModalMenu.showDidCommitSomeday(event); // Show the member list succeed to commit in specific day
                // 2 translation cmds below are available only in non-English mode
                case JDAController.CMD_TRANSLATE_EN_TO_MAIN   -> ModalMenu.showTranslate_EN_to_MAIN(event); // English → Main language translation
                case JDAController.CMD_TRANSLATE_MAIN_TO_EN   -> ModalMenu.showTranslate_MAIN_to_EN(event); // Main language → English translation
                case JDAController.CMD_ABOUT -> { // Introduce of JandiChecker
                    event.deferEdit().queue(); // Set defer
                    result = pr.getInformation();
                }
                case JDAController.CMD_CLOSE -> {
                    event.getMessage().delete().queue();
                    return;
                }
                default -> throw new Exception();
            }

        } catch(Exception e) {
            result = pr.l("err_failedToGetInfo");
        }

        // If you didn't call any modal and bring here just String,
        // the switch block above won't be run. Code below runs instead of that.
        // On the other hand, if you called the modal or something
        // this block won't be executed. (because result is null as default value).
        if(!StringUtils.isEmpty(result)) {

            // Show result
            // event.reply(result).queue();
            event.getChannel().sendMessage(result).queue();

            // Remove defer message and its button menu panel
            // event.getHook().sendMessage(result).queue();
            // event.getHook().editOriginal(result).queue();
            event.getMessage().delete().queue(); // Remove all the menu and the original messages

        }

    }

}
