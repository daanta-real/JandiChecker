package chatGPT;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;

// ChatGPT API related services
@Slf4j
public class ChatService {

    // Utility class
    private ChatService() {
    }

    // send ChatGPT request with "inputTxt" String and return its response
    public static String getChatResponse(String inputTxt) {

        // 1. variable
        StringBuilder sb = new StringBuilder();

        // 2. Send request
        OpenAiService service = new OpenAiService(Initializer.getToken_chatGPTAPI());
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(inputTxt) // The question
                .model("text-davinci-001")   // Strongest AI (has very high risk of timeout)
                .maxTokens(500)              // Max length of answer string
                .temperature(0d)             // Most strict answer
                .echo(false)                 // Don't print the caller's question again
                .build();

        // 3. Get response to StringBuilder instance
        service.createCompletion(completionRequest).getChoices().forEach(response -> {
            sb.append(response.getText());
            sb.append("\n");
        });

        // 4. Clean and retrun it
        return sb.toString().replaceAll("", "\n");

    }

}
