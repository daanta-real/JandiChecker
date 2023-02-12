package jda.menu;

import jda.JDAController;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import translate.TranslationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static init.Initializer.pr;

public class ButtonMenu extends ListenerAdapter {

    private static final Button[] btn = new Button[] {
        Button.success(JDAController.CMD_ME, pr.l("menu_me")).withEmoji(Emoji.fromUnicode("📊")),
        Button.success(JDAController.CMD_JANDIYA, pr.l("menu_heyJandi")).withEmoji(Emoji.fromUnicode("\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93")),

        Button.primary(JDAController.CMD_LIST_YESTERDAY_SUCCEED, pr.l("menu_listYesterdaySucceed")).withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),
        Button.primary(JDAController.CMD_LIST_TODAY_SUCCEED, pr.l("menu_listTodaySucceed")).withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),

        Button.secondary(JDAController.CMD_NAME, pr.l("menu_mapByName")).withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),
        Button.secondary(JDAController.CMD_ID, pr.l("menu_mapByID")).withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),

        Button.secondary(JDAController.CMD_LIST_YESTERDAY_FAIL, pr.l("menu_listYesterdayFail")).withEmoji(Emoji.fromUnicode("❕")),
        Button.secondary(JDAController.CMD_LIST_BY_DATE, pr.l("menu_listByDate")).withEmoji(Emoji.fromUnicode("\uD83D\uDDD3")),

        Button.secondary(JDAController.CMD_ABOUT, pr.l("menu_about")).withEmoji(Emoji.fromUnicode("❔")),
        Button.danger(JDAController.CMD_CLOSE, pr.l("menu_close")).withEmoji(Emoji.fromUnicode("✖"))
    };

    public static void showButtonMenues(MessageReceivedEvent e) {

        MessageCreateAction a = e.getChannel()
                .sendMessage("\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 %s \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93"
                        .formatted(pr.l("menu_jandiHereHowCanIHelpYou")))
                .addActionRow(makeRow(btn[0], btn[1]))
                .addActionRow(makeRow(btn[2], btn[3]))
                .addActionRow(makeRow(btn[4], btn[5]))
                .addActionRow(makeRow(btn[6], btn[7]));

        if (!TranslationService.mainLanguageLong.equals("English")) { // Non-English mode
            Button enToMain = Button.secondary(JDAController.CMD_TRANSLATE_EN_TO_MAIN, pr.l("menu_ENToMain")).withEmoji(Emoji.fromUnicode("\uD83C\uDDF0\uD83C\uDDF7"));
            Button mainToEn = Button.secondary(JDAController.CMD_TRANSLATE_MAIN_TO_EN, pr.l("menu_MainToEN")).withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8"));
            a.addActionRow(makeRow(enToMain, mainToEn, btn[8], btn[9]));
        } else { // English mode
            a.addActionRow(makeRow(btn[8], btn[9]));
        }

        a.queue();

    }

    private static List<Button> makeRow(Button ...button) {
        List<Button> row = new ArrayList<>();
        Collections.addAll(row, button);
        return row;
    }

}
