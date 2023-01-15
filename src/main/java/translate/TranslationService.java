package translate;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import utils.CommonUtils;

import java.io.ByteArrayInputStream;

@Slf4j
public class TranslationService {

    // Field
    private static Translate translate;

    // Make translate instance
    public static void init() throws Exception {
        translate = TranslateOptions.newBuilder().setCredentials(
                ServiceAccountCredentials.fromStream(
                        new ByteArrayInputStream(Initializer.props.get("GoogleCloudToken").getBytes())
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
        String result = executeTranslate("ko", "en", text);
        return CommonUtils.unescapeHTMLEntity(result);
    }

    // Translation method: Eng to Kor
    public static String translateEngToKor(String text) {
        String result = executeTranslate("en", "ko", text);
        return CommonUtils.unescapeHTMLEntity(result);
    }

}
