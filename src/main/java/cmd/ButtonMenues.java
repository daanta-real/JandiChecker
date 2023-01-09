package cmd;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ButtonMenues extends ListenerAdapter {

    private static final Button[] btn = new Button[] {
            Button.success("mine", "내 커밋 정보 조회").withEmoji(Emoji.fromUnicode("📊")),
            Button.secondary("byName", "이름으로 조회").withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),
            Button.secondary("byId", "ID로 조회").withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),
            Button.primary("listToday", "오늘 커밋 성공한 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),
            Button.primary("listYesterday", "어제 커밋 성공한 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDCAF")),
            Button.secondary("listYesterdayFail", "어제 커밋 건너뛴 사람").withEmoji(Emoji.fromUnicode("❕")),
            Button.secondary("listByDate", "특정일 성공한 사람").withEmoji(Emoji.fromUnicode("\uD83D\uDDD3")),
            Button.primary("about", "소개/도움말").withEmoji(Emoji.fromUnicode("❔"))
    };

    public static void showButtonMenues(MessageReceivedEvent e) {
        e.getChannel().sendMessage("\uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 무엇을 도와드릴까요? \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93")
                .addActionRow(makeRow(btn[0]))
                .addActionRow(makeRow(btn[1], btn[2]))
                .addActionRow(makeRow(btn[3], btn[4]))
                .addActionRow(makeRow(btn[5], btn[6]))
                .addActionRow(makeRow(btn[7]))
                .queue();
    }

    private static List<Button> makeRow(Button ...button) {
        List<Button> row = new ArrayList<>();
        Collections.addAll(row, button);
        return row;
    }

}
