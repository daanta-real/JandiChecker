package translate;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;

@Slf4j
public class TranslationService {

    // Field
    private static Translate translate;

    // Make translate instance
    public static void init() throws Exception {
        translate = TranslateOptions.newBuilder().setCredentials(
                ServiceAccountCredentials.fromStream(
                        new FileInputStream("googleAPIKey.json")
                )
        ).build().getService();
    }

    // Core translate method
    private static String executeTranslate(String source, String target, String text) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(source),
                Translate.TranslateOption.targetLanguage(target)
        );
        return translation.getTranslatedText();
    }

    // Translation method: Kor to Eng
    public static String translateKorToEng(String text) {
        return executeTranslate("ko", "en", text);
    }

    // Translation method: Eng to Kor
    public static String translateEngToKor(String text) {
        return executeTranslate("en", "ko", text);
    }

}
