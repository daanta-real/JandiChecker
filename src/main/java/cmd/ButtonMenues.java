package cmd;

import jda.JdaController;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ButtonMenues extends ListenerAdapter {

    private static final Button[] btn = new Button[] {
            Button.success(JdaController.CMD_ME, "내 잔디 조회").withEmoji(Emoji.fromUnicode("📊")),
            Button.success(JdaController.CMD_JANDIYA, "AI에게 질문").withEmoji(Emoji.fromUnicode("\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93")),
            Button.secondary(JdaController.CMD_NAME, "이름으로 잔디 조회").withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),
            Button.secondary(JdaController.CMD_ID, "ID로 잔디 조회").withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),
            Button.primary(JdaController.CMD_LIST_YESTERDAY_SUCCESS, "어제 잔디 심은 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),
            Button.secondary(JdaController.CMD_LIST_YESTERDAY_FAIL, "어제 잔디 건너뛴 사람").withEmoji(Emoji.fromUnicode("❕")),
            Button.primary(JdaController.CMD_LIST_TODAY_SUCCESS, "오늘 잔디 심은 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),
            Button.secondary(JdaController.CMD_LIST_BY_DATE, "특정일 잔디 심은 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDDD3")),
            Button.primary(JdaController.CMD_ABOUT, "대하여..").withEmoji(Emoji.fromUnicode("❔")),
            Button.danger(JdaController.CMD_CLOSE, "닫기..").withEmoji(Emoji.fromUnicode("✖"))
    };

    public static void showButtonMenues(MessageReceivedEvent e) {
        e.getChannel().sendMessage("\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 잔디 등장. 무엇을 도와드릴까요? \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93")
                .addActionRow(makeRow(btn[0], btn[1]))
                .addActionRow(makeRow(btn[2], btn[3]))
                .addActionRow(makeRow(btn[4], btn[5]))
                .addActionRow(makeRow(btn[6], btn[7]))
                .addActionRow(makeRow(btn[8], btn[9]))
                .queue();
    }

    private static List<Button> makeRow(Button ...button) {
        List<Button> row = new ArrayList<>();
        Collections.addAll(row, button);
        return row;
    }

}
