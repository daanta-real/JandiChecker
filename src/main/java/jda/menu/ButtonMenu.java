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

import static init.Pr.pr;

public class ButtonMenu extends ListenerAdapter {

    private static final Button[] btn = new Button[] {
        Button.success(JDAController.instance.getCmdMe(), pr.l("menu_me")).withEmoji(Emoji.fromUnicode("📊")),
        Button.success(JDAController.instance.getCmdJandiya(), pr.l("menu_heyJandi")).withEmoji(Emoji.fromUnicode("\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93")),

        Button.primary(JDAController.instance.getCmdListYesterdaySucceed(), pr.l("menu_listYesterdaySucceed")).withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),
        Button.primary(JDAController.instance.getCmdListTodaySucceed(), pr.l("menu_listTodaySucceed")).withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),

        Button.secondary(JDAController.instance.getCmdName(), pr.l("menu_mapByName")).withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),
        Button.secondary(JDAController.instance.getCmdId(), pr.l("menu_mapByID")).withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),

        Button.secondary(JDAController.instance.getCmdListYesterdayFail(), pr.l("menu_listYesterdayFail")).withEmoji(Emoji.fromUnicode("❕")),
        Button.secondary(JDAController.instance.getCmdListByDate(), pr.l("menu_listByDate")).withEmoji(Emoji.fromUnicode("\uD83D\uDDD3")),

        Button.secondary(JDAController.instance.getCmdAbout(), pr.l("about")).withEmoji(Emoji.fromUnicode("❔")),
        Button.danger(JDAController.instance.getCmdClose(), pr.l("close")).withEmoji(Emoji.fromUnicode("✖"))
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
            Button enToMain = Button.secondary(JDAController.instance.getCmdTranslateEnToMain(), pr.l("cmdName_ENToMain")).withEmoji(Emoji.fromUnicode("\uD83C\uDDF0\uD83C\uDDF7"));
            Button mainToEn = Button.secondary(JDAController.instance.getCmdTranslateMainToEn(), pr.l("cmdName_MainToEN")).withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8"));
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
