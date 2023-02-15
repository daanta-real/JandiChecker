package jda.interaction;

import chat.ChatService;
import jda.JDAMsgService;
import jda.menu.ButtonMenu;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static init.Initializer.pr;

// Listen the messages and take the action in response
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
            String questionMain = message.substring(4);
            String answerMain = ChatService.getChatAnswer(questionMain);
            String saysAdded = "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93```%s```"
                    .formatted(pr.l("chat_GPTSays"), answerMain);
            JDAMsgService.send(event, saysAdded); // No additional executions, just directly show the answer message to the chat channel.
        }

    }

}
