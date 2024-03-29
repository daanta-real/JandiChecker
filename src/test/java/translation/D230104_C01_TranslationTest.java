package translation;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import translate.TranslationService;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class D230104_C01_TranslationTest {

    // Field
    private static Translate translate;

    // Make translate instance
    @BeforeAll
    static void init() throws Exception {
        translate = TranslateOptions.newBuilder().setCredentials(
                ServiceAccountCredentials.fromStream(
                        new FileInputStream("googleAPIKey.json")
                )
        ).build().getService();
    }

    // Core translate method
    private String executeTranslate(String source, String target, String text) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(source),
                Translate.TranslateOption.targetLanguage(target)
        );
        return translation.getTranslatedText();
    }

    // Translation method: Main to Eng
    private String translateMainToEng(String text) { return executeTranslate(TranslationService.mainLanguageShort, "en", text); }

    // Translation method: Eng to Main
    private String translateEngToMain(String text) {
        return executeTranslate("en", TranslationService.mainLanguageShort, text);
    }

    @Test
    public void main() {

        assertNotNull(translate);

        String text = "우리나라의 미래는 IT산업의 부흥에 달려 있다.";
        log.debug(translateMainToEng(text));

        String text2 = "You have passed my exam";
        log.debug(translateEngToMain(text2));

    }

}