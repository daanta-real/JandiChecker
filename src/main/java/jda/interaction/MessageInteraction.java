package jda.interaction;

import chat.ChatService;
import com.google.cloud.translate.TranslateException;
import jda.JDAMsgService;
import jda.menu.ButtonMenu;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static init.Pr.pr;

// Listen the messages and take the action in response
@Slf4j
public class MessageInteraction {

    public static void run(MessageReceivedEvent event) {

        // Get the message
        String message = event.getMessage().getContentRaw();
        if(message.isEmpty()) return;

        // Branch prossesing by the command keyword

        // 1. If message is exactly same as the command execution keyword, show the menu panel to the chatting channel
        if(message.toLowerCase().equals(pr.l("commandExecutionKeyword"))) {
            ButtonMenu.showButtonMenues(event);
        }
        // 2. If message starts with the command execution keyword and has a space next to it,
        //    it might be the direct order with some option.
        //    Currently, the order like this type is only stands for the ChatGPT AI quick answer command.
        //    So if the message is in this format, execute it.
        else if(message.toLowerCase().startsWith(pr.l("commandExecutionKeyword") + " ")) {
            String result;
            try {
                String questionMain = message.substring(4);
                String answerMain = ChatService.getChatAnswer(questionMain);
                result = "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93```%s```"
                        .formatted(pr.l("chat_GPTSays"), answerMain);
            } catch(TranslateException e) {
                log.debug("\n==========\n\n");
                log.debug("TRANSLATE ERROR: {}", e.getReason());
                log.debug(e.getMessage());
                log.debug("\n\n==========\n");
                result = pr.l("err_fromAPI");
            } catch(Exception e) {
                log.debug("Etc error has occured.");
                result = pr.l("err_fromAPI");
            }
            JDAMsgService.send(event, result); // No additional executions, just directly show the answer message to the chat channel.
        }

    }

}
