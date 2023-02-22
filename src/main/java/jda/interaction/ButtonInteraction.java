package jda.interaction;

import cmd.CmdService;
import jda.JDAController;
import jda.menu.ModalMenu;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.lang3.StringUtils;

import static init.Pr.pr;

@Slf4j
public class ButtonInteraction {

    public static void run(ButtonInteractionEvent event) {

        // Get command and options
        String cmd = event.getComponentId(); // Command
        log.debug("[[[ COMMAND RECEIVED BY PUSHING BUTTON ]]]: [{}]", cmd);

        if (StringUtils.equals(cmd, JDAController.instance.getCmdAbout())) {

            // Introduce of JandiChecker
            event.deferEdit().queue(); // Set defer
            String result1 = pr.getInformation1();
            // event.reply(result).queue();
            event.getChannel().sendMessage(result1).queue();

            // Remove defer message and its button menu panel
            // event.getHook().sendMessage(result).queue();
            // event.getHook().editOriginal(result).queue();
            event.getMessage().delete().queue(); // Remove all the menu and the original messages

            String result2 = pr.getInformation2();
            event.getChannel().sendMessage(result2).queue();

        } else {

            String result = null;

            try {

                // Run each command
                if (StringUtils.equals(cmd, JDAController.instance.getCmdMe())) {
                    // Convert the instant got from Interaction to a spinner
                    // I guess I should use .deferEdit() not .deferReply().
                    // Because if I use .deferReply() the answer will be got as reply not the add itional message.
                    // After that the reply says "The message has deleted" attached bottom of the original message,
                    // and the spinner cannot be deleted with no other ways.
                    event.deferEdit().queue();
                    result = CmdService.getJandiMapStringOfMine(event.getUser()); // Get my Commit map info
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdJandiya())) {
                    ModalMenu.getChatAnswer(event); // The AI answers for general questions
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdListYesterdaySucceed())) {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.getDidCommitStringYesterday(); // Show the member list succeed to commit yesterday
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdListTodaySucceed())) {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.getDidCommitStringToday(); // Show the member list succeed to commit today
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdName())) {
                    ModalMenu.showJandiMapByName(event); // Show the total commit info of the member by the specific name
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdId())) {
                    ModalMenu.showJandiMapById(event); // Show the total commit info of the member by the GitHub ID
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdListYesterdayFailed())) {
                    event.deferEdit().queue(); // Set defer
                    result = CmdService.getNotCommittedStringYesterday(); // Show the member list failed to commit yesterday
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdListByDate())) {
                    ModalMenu.showDidCommitSomeday(event); // Show the member list succeed to commit in specific day
                }
                // 2 translation cmds below are available only in non-English mode
                else if (StringUtils.equals(cmd, JDAController.instance.getCmdTranslateEnToMain())) {
                    ModalMenu.showTranslate_EN_to_MAIN(event); // English → Main language translation
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdTranslateMainToEn())) {
                    ModalMenu.showTranslate_MAIN_to_EN(event); // Main language → English translation
                } else if (StringUtils.equals(cmd, JDAController.instance.getCmdClose())) {
                    event.getMessage().delete().queue();
                    return;
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                result = pr.l("err_failedToGetInfo");
            }

            // If you didn't call any modal and bring here just String,
            // the switch block above won't be run. Code below runs instead of that.
            // On the other hand, if you called the modal or something
            // this block won't be executed. (because result is null as default value).
            if (!StringUtils.isEmpty(result)) {

                // Show result
                event.getChannel().sendMessage(result).queue();

                // Remove defer message and its button menu panel
                event.getMessage().delete().queue(); // Remove all the menu and the original messages

            }
        }

    }

}
