package chatGPT;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import translate.TranslationService;

import java.util.List;

// ChatGPT API related services
@Slf4j
public class ChatService {

    // Utility class
    private ChatService() {
    }

    // send ChatGPT request with "inputTxt" String and return its response
    public static String getChatAnswerByQuestion(List<String> inputTxtList) {

        // 1. Prepare
        String questionKor = StringUtils.join(inputTxtList, " ");
        log.debug("접수된 원본 질문: \"{}\" (길이 {})", questionKor, questionKor.length());

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

        // 6. Return result
        return "\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93ChatGPT AI님 가라사대...\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93```" + answerKor + "```";

    }

}
