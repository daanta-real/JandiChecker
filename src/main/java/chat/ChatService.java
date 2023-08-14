package chat;

import com.google.cloud.translate.TranslateException;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.extern.slf4j.Slf4j;
import translate.TranslationService;
import utils.CommonUtils;

import static init.Pr.pr;

// ChatGPT API related services
@Slf4j
public class ChatService {

    // Utility class
    private ChatService() {
    }

    // ChatGPT request core
    private static String requestChatGPT(String questionEng) {

        // 1. Request
        StringBuilder sb = new StringBuilder();
        OpenAiService service = new OpenAiService(pr.getToken_ChatGPT());
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(questionEng) // The question
                .model("text-davinci-001")   // Strongest AI (has very high risk of timeout)
                .maxTokens(1900)              // Max length of answer string
                .temperature(1d)             // Changed Most strict answer(0d) to Chaos answer(1d)
                .echo(false)                 // Don't print the caller's question again
                .build();

        // 2. Convert the answer in StringBuilder instance
        service.createCompletion(completionRequest).getChoices().forEach(response -> {
            log.debug(pr.l("chat_response"), response.getText());
            sb.append(response.getText());
            sb.append("\n");
        });

        // 3. Make clean the answer string
        String answer = sb.toString().replaceAll("\n\n", "\n");
        String answerEscaped = CommonUtils.unescapeHTMLEntity(answer);
        log.debug(pr.l("chat_theAnswer"), answerEscaped);

        return answerEscaped;

    }

    // Answering method core
    public static String getChatAnswer(String question) {

        // 1. Prepare
        log.debug(pr.l("chat_query"), question, question.length());

        // 2. Use ChatGPT

        // 2-1. If your selected language is not English, apply bridge translation
        String answer;
        if(!TranslationService.mainLanguageLong.equals("English")) {

            try {
                // 2. Mother language -> English
                String questionEng = TranslationService.translateMainToEng(question);
                String unescapedEng = CommonUtils.unescapeHTMLEntity(questionEng);
                log.debug("Translated text to English(length = {}): {}", unescapedEng.length(), unescapedEng);

                String answerEng = requestChatGPT(unescapedEng);

                answer = TranslationService.translateEngToMain(answerEng);
            } catch(TranslateException e) {
                log.debug("\n==========\n\n");
                log.debug("TRANSLATE ERROR: {}", e.getReason());
                log.debug(e.getMessage());
                log.debug("\n\n==========\n");
                return pr.l("err_fromAPI");
            } catch(Exception e) {
                log.debug("Etc error has occured.");
                return pr.l("err_fromAPI");
            }

        }
        // 2-2. If mother language is set as English, just translate it
        else {
            answer = requestChatGPT(question);
        }
        log.debug(pr.l("chat_theAnswer"), answer);

        // 6. If the result have unintentional chars("? "), trim it
        if(answer.startsWith("? ")) {
            answer = answer.substring(2);
        }

        // 7. Return result
        return answer;

    }

}
