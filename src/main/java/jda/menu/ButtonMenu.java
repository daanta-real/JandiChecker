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

public class ButtonMenu extends ListenerAdapter {

    private static final Button[] btn = new Button[] {
        Button.success(JDAController.CMD_ME, "내 잔디 조회").withEmoji(Emoji.fromUnicode("📊")),
        Button.success(JDAController.CMD_JANDIYA, "AI에게 질문").withEmoji(Emoji.fromUnicode("\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93")),

        Button.primary(JDAController.CMD_LIST_YESTERDAY_SUCCESS, "어제 잔디 심은 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),
        Button.primary(JDAController.CMD_LIST_TODAY_SUCCESS, "오늘 잔디 심은 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),

        Button.secondary(JDAController.CMD_NAME, "이름으로 잔디 조회").withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),
        Button.secondary(JDAController.CMD_ID, "ID로 잔디 조회").withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),

        Button.secondary(JDAController.CMD_LIST_YESTERDAY_FAIL, "어제 잔디 건너뛴 사람").withEmoji(Emoji.fromUnicode("❕")),
        Button.secondary(JDAController.CMD_LIST_BY_DATE, "특정일 잔디 심은 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDDD3")),

        Button.secondary(JDAController.CMD_ABOUT, "대하여..").withEmoji(Emoji.fromUnicode("❔")),
        Button.danger(JDAController.CMD_CLOSE, "닫기").withEmoji(Emoji.fromUnicode("✖"))
    };

    public static void showButtonMenues(MessageReceivedEvent e) {

        MessageCreateAction a = e.getChannel().sendMessage("\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 잔디 등장. 무엇을 도와드릴까요? \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93")
                .addActionRow(makeRow(btn[0], btn[1]))
                .addActionRow(makeRow(btn[2], btn[3]))
                .addActionRow(makeRow(btn[4], btn[5]))
                .addActionRow(makeRow(btn[6], btn[7]));

        if (!TranslationService.mainLanguageLong.equals("English")) { // Non-English mode
            Button enToMain = Button.secondary(JDAController.CMD_TRANSLATE_EN_TO_MAIN, "영어→한글").withEmoji(Emoji.fromUnicode("\uD83C\uDDF0\uD83C\uDDF7"));
            Button mainToEn = Button.secondary(JDAController.CMD_TRANSLATE_MAIN_TO_EN, "한글→영어").withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8"));
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
