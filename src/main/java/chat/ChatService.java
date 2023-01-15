package chat;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import translate.TranslationService;
import utils.CommonUtils;

// ChatGPT API related services
@Slf4j
public class ChatService {

    // Utility class
    private ChatService() {
    }

    // Answering method core
    public static String getChatAnswer(String questionKor) {

        // 1. Prepare
        log.debug("접수된 원본 질문: <<<{}>>> (길이 {})", questionKor, questionKor.length());

        // 2. KOR -> ENG
        String questionEng = TranslationService.translateKorToEng(questionKor);
        String unescapedEng = CommonUtils.unescapeHTMLEntity(questionEng);

        log.debug("영어로 번역된 질문: <<<{}>>> (길이 {})", unescapedEng, unescapedEng.length());

        // 3. Make inquire
        StringBuilder sb = new StringBuilder();
        OpenAiService service = new OpenAiService(Initializer.props.get("ChatGPTToken"));
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(unescapedEng) // The question
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
        String unescapedKor = CommonUtils.unescapeHTMLEntity(answerKor);
        log.debug("한국어로 번역된 답변: <<<{}>>>", unescapedKor);

        // 6. If the result have unintentional chars("? "), trim it
        if(unescapedKor.startsWith("? ")) {
            unescapedKor = unescapedKor.substring(2);
        }

        // 7. Return result
        return unescapedKor;

    }

}
