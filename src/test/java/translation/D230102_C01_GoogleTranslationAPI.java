package translation;
// Imports the Google Cloud Translation library.
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class D230102_C01_GoogleTranslationAPI {

    @Test
    public void runTranslate() {
        log.debug("실험 시작..");

        try {
            GoogleTranslation.translate();
        } catch(Exception e) {
            log.debug("에러났엉:\n");
            log.error(ExceptionUtils.getStackTrace(e));
        }

        log.debug("실험 종료..");
    }

}

@Slf4j
class GoogleTranslation {

    private GoogleTranslation() {
    }

    // Set and pass variables to overloaded translateText() method for translationTest.
    public static void translate() throws IOException {

        log.debug("translate() 시작");

        // Developers, Replace these variables before running the sample.
        String projectId = "YOUR-PROJECT-ID";
        log.debug("projectId = {}", projectId);

        // Supported Languages: https://cloud.google.com/translate/docs/languages
        String targetLanguage = "your-target-language";
        log.debug("targetLanguage = {}", targetLanguage);

        String text = "your-text";
        log.debug("text = {}", text);

        translate(projectId, targetLanguage, text);

    }

    // Translate text to target language.
    public static void translate(String projectId, String targetLanguage, String text) throws IOException {

        log.debug("translationTest(A, B, C) 시작..");
/*
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (TranslationServiceClient client = TranslationServiceClient.create()) {

            log.debug("client 선언됨");

            // Supported Locations: `global`, [glossary location], or [model location]
            // Glossaries must be hosted in `us-central1`
            // Custom Models must use the same location as your model. (us-central1)
            LocationName parent = LocationName.of(projectId, "global");
            log.debug("parent = ", parent.toString());

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(text)
                            .build();
            log.debug("request = ", request.toString());

            log.debug("진짜 번역 실시합니다");
            TranslateTextResponse response = client.translateText(request);
            log.debug("번역을 완료했습니다.");

            // Display the translationTest for each input text provided
            log.debug("번역 결과를 표시합니다");
            for (Translation translationTest: response.getTranslationsList()) {
                System.out.printf("Translated text: %s\n", translationTest.getTranslatedText());
            }

            log.debug("최종 번역 완료");

        }*/

    }

}