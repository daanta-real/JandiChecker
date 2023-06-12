package translate;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateException;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.extern.slf4j.Slf4j;
import utils.CommonUtils;

import java.io.ByteArrayInputStream;

import static init.Pr.pr;

@Slf4j
public class TranslationService {

    // Field
    private static Translate translate;
    public static String mainLanguageLong;
    public static String mainLanguageShort;

    // Make translate instance
    public static void init() throws Exception {

        // Set mother language
        // JandiChecker supports 6 languages
        // English, Korean, Japanese, Chinese(Simplified), Chinese(Traditional)
        // You can change your language settings with 'settings.xlsx'
        mainLanguageLong = pr.getLanguage();
        mainLanguageShort
                = "Korean".equals(mainLanguageLong)               ? "ko"
                : "English".equals(mainLanguageLong)              ? "en"
                : "Japanese".equals(mainLanguageLong)             ? "ja"
                : "Chinese(Simplified)".equals(mainLanguageLong)  ? "zh-CN"
                : "Chinese(Traditional)".equals(mainLanguageLong) ? "zh-TW"
                : "en"; // English default

        // initialize translator instance
        translate = TranslateOptions.newBuilder().setCredentials(
                ServiceAccountCredentials.fromStream(
                        new ByteArrayInputStream(pr.getToken_GoogleCloud().getBytes())
                )
        ).build().getService();
    }

    // Core translate method
    private static String executeTranslate(String source, String target, String text) {
        try {
            Translation translation = translate.translate(
                    text,
                    Translate.TranslateOption.sourceLanguage(source),
                    Translate.TranslateOption.targetLanguage(target)
            );
            return translation.getTranslatedText();
        } catch(TranslateException e) {
            log.debug("\n==========\n\n");
            log.debug("TRANSLATE ERROR: {}", e.getReason());
            log.debug(e.getMessage());
            log.debug("\n\n==========\n");
            return null;
        } catch(Exception e) {
            log.debug("Etc error has occured.");
            return null;
        }
    }

    // Translation method: Main to Eng
    public static String translateMainToEng(String text) {
        String result = executeTranslate(mainLanguageShort, "en", text);
        return CommonUtils.unescapeHTMLEntity(result);
    }

    // Translation method: Eng to Main
    public static String translateEngToMain(String text) {
        String result = executeTranslate("en", mainLanguageShort, text);
        return CommonUtils.unescapeHTMLEntity(result);
    }

}
