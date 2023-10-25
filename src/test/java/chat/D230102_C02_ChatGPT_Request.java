package chat;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import init.Pr;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import init.Initializer;
@Slf4j
class D230102_C02_ChatGPT_Request {

    static Pr pr;

    @BeforeAll
    public static void init() throws Exception {

        // Load all preferences
        log.info("<<< INITIALIZING JandiChecker >>>");
        Initializer.ready(true);

        pr = init.Pr.pr;

    }

    @Test
    public void chatTest() throws Exception {

        // 1. Declare
        Initializer.ready(false);
        String inputTxt;

        // 2. Input
//        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
//            StringBuilder sb = new StringBuilder();
//            String scanned = br.readLine();
//            if(!StringUtils.isEmpty(scanned)) input = scanned;
//        }
//        inputTxt = "대한민국의 올해 경제정책에 대해서 설명하시오";
        inputTxt = "Please explain Korean economic policy this year.";

        OpenAiService service = new OpenAiService(pr.getToken_ChatGPT());
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(inputTxt) // The question
                .model("text-davinci-001")   // Strongest AI (has very high risk of timeout)
//                .model("text-curie-001")     // Strong AI (has high risk of timeout)
//                .model("text-babbage-001")   // Normal AI (small risk)
//                .model("text-ada-001")       // Super Simple AI (almost no risk)
                .maxTokens(500)              // Max length of answer string
                .temperature(0d)             // Most strict answer
                .echo(false)                 // Don't print the caller's question again
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(response -> pr(response.getText()));

    }

    public void pr(String text) {
        text = text.replaceAll("\n\n", " ");
        log.debug("로그: <<<{}>>>", text);
    }

}
