package chat;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import init.Initializer;
import jda.JDAMsgSender;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.lang3.StringUtils;
import translate.TranslationService;
import utils.CommonUtils;

import java.util.Objects;

// ChatGPT API related services
@Slf4j
public class ChatService {

    // Utility class
    private ChatService() {
    }

    // Received event from msg event
    public static String getChatAnswerByMsgCmd(String questionKor) {
        String answerKor = getChatAnswerByQuestion(questionKor);
        String addSays = "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 ChatGPT AI님 가라사대... \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93```" + answerKor + "```";
        String unescaped = CommonUtils.unescapeHTMLEntity(addSays);
        return JDAMsgSender.msgTrim(unescaped);
    }

    // Received event from button event
    public static String getChatAnswerWithButton(ButtonInteractionEvent event) {
        return null; // TODO
    }

    // Received event from slash event
    public static String getChatAnswerWithSlash(SlashCommandInteractionEvent event, String questionKor) {

        // Make name and answer string
        User user = Objects.requireNonNull(event.getMember()).getUser();
        String name = user.getName();
        if(StringUtils.isEmpty(name)) return "질문자의 ID가 명확하지 않습니다.";
        String answerKor = getChatAnswerByQuestion(questionKor);
        String unescaped = CommonUtils.unescapeHTMLEntity(answerKor);

        // Make chat message and return
        return """
                🤔 %s님의 질문... 🤔```md
                %s
                ```
                \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 ChatGPT AI님 가라사대... \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                ```
                %s
                (📌 "잔디야 bla bla..." 이런 식으로 질문하시면 약간 더 긴 답변을 받을 수 있습니다!)
                ```
                """.formatted(name, questionKor, unescaped);

    }

    // Answering method core
    public static String getChatAnswerByQuestion(String questionKor) {

        // 1. Prepare
        log.debug("접수된 원본 질문: <<<{}>>> (길이 {})", questionKor, questionKor.length());

        // 2. KOR -> ENG
        String questionEng = TranslationService.translateKorToEng(questionKor);
        log.debug("영어로 번역된 질문: <<<{}>>> (길이 {})", questionEng, questionEng.length());

        // 3. Make inquire
        StringBuilder sb = new StringBuilder();
        OpenAiService service = new OpenAiService(Initializer.getToken_chatGPTAPI());
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(questionEng) // The question
                .model("text-davinci-001")   // Strongest AI (has very high risk of timeout)
                .maxTokens(500)              // Max length of answer string
                .temperature(0d)             // Most strict answer
                .echo(false)                 // Don't print the caller's question again
                .build();

        // 4. Get response to StringBuilder instance
        service.createCompletion(completionRequest).getChoices().forEach(response -> {
            log.debug("응답 수신: <<<{}>>>", response.getText());
            sb.append(response.getText());
            sb.append("\n");
        });
        String answerEng = sb.toString().replaceAll("\n\n", "\n");
        log.debug("영어 답변: <<<{}>>>", answerEng);

        // 5. ENG -> KOR
        String answerKor = TranslationService.translateEngToKor(answerEng);
        log.debug("한국어로 번역된 답변: <<<{}>>>", answerKor);

        // 6. If the result have unintentional chars("? "), trim it
        if(answerKor.startsWith("? ")) {
            answerKor = answerKor.substring(2);
        }

        // 7. Return result
        return answerKor;

    }

}
