package jda.interaction;

import chat.ChatService;
import cmd.CmdService;
import init.Initializer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

// 모달 관련된 각종 동작(UI 생성, 표시 및 이에 대한 사용자 입력에 따른 후속 동작까지)
@Slf4j
public class ModalInteraction {

    // Main interaction
    public static void run(ModalInteractionEvent event) {

        event.deferReply().queue();

        String result;
        try {
            switch (event.getModalId()) {
                case "showJandiMapByName" -> {

                    // Prepare
                    ModalMapping m = event.getValue("showJandiMapByNameText");
                    assert m != null;
                    String targetMemberName = m.getAsString();
                    String gitHubID = Initializer.getGitHubIDByMemberName(targetMemberName);

                    // Compute
                    result = CmdService.getJandiMapStringByIdAndName(targetMemberName, gitHubID);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showJandiMapById" -> {

                    // Prepare
                    ModalMapping m = event.getValue("showJandiMapByIdText");
                    assert m != null;
                    String id = m.getAsString();

                    // Compute
                    result = CmdService.getJandiMapStringByById(id);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "getChatAnswer" -> {

                    // Prepare
                    User user = Objects.requireNonNull(event.getMember()).getUser();
                    String discordID = user.getAsTag();
                    String memberName = Initializer.getMemberNameByDiscordID(discordID);
                    ModalMapping m = event.getValue("getChatAnswerText");
                    assert m != null;
                    String questionKor = m.getAsString();
                    log.debug("그룹원 {}님(디코ID {})의 질문: {}", memberName, discordID, questionKor);

                    // Compute
                    String answerKor = ChatService.getChatAnswer(questionKor);
                    log.debug("답변: {}", answerKor);
                    result = """
                        🤔 %s님의 질문... 🤔```md
                        %s
                        ```
                        \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93 ChatGPT AI님 가라사대... \uD83D\uDC69\uD83C\uDFFB\u200D\uD83C\uDF93
                        ```
                        %s
                                            
                        📌 "잔디야 bla bla..." 이런 식으로 질문하시면 약간 더 긴 답변을 받을 수 있습니다.
                        ```
                        """.formatted(memberName, questionKor, answerKor);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                case "showDidCommitSomeday" -> {

                    // Prepare
                    ModalMapping m = event.getValue("showDidCommitSomedayText");
                    assert m != null;
                    String date = m.getAsString();

                    // Compute
                    result = CmdService.getDidCommitStringSomeday(date);

                    // Show the result
                    event.getHook().sendMessage(result).queue();

                }
                default -> result = "모듈 온 게 없는데요..";
            }

            // If the result is null or blank String then throw an AssertException
            assert !StringUtils.isEmpty(result);

        } catch(Exception e) {
            result = "정확히 입력해 주세요.";
            event.getHook().sendMessage(result).queue();
        }


    }

    // 특정 그룹원명의 종합 잔디정보를 리턴
    public static void showJandiMapByName(ButtonInteractionEvent event) {

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput name = TextInput.create("showJandiMapByNameText", "잔디체커", TextInputStyle.SHORT)
                .setMinLength(1)
                .setMaxLength(10)
                .setLabel("그룹원 이름")
                .setPlaceholder("최대 10글자")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("showJandiMapByName", "그룹원명 입력")
                .addActionRows(ActionRow.of(name))
                .build();

        event.replyModal(modal).queue();

    }

    // 특정 ID의 종합 잔디정보를 리턴
    public static void showJandiMapById(ButtonInteractionEvent event) {

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput id = TextInput.create("showJandiMapByIdText", "잔디체커", TextInputStyle.SHORT)
                .setMinLength(1)
                .setMaxLength(20)
                .setLabel("GitHub ID")
                .setPlaceholder("최대 20글자")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("showJandiMapById", "아이디 입력")
                .addActionRows(ActionRow.of(id))
                .build();

        event.replyModal(modal).queue();

    }

    // ChatGPT를 불러 답변을 받아 리턴
    public static void getChatAnswer(ButtonInteractionEvent event) {

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput id = TextInput.create("getChatAnswerText", "잔디체커", TextInputStyle.PARAGRAPH)
                .setMinLength(1)
                .setMaxLength(200)
                .setLabel("하고 싶은 질문")
                .setPlaceholder("최대 300글자. 글자수 제한 없이 하려면 이 버튼 메뉴를 통하지 말고 채팅으로 직접 \"잔디야 [질문내용]\"이라고 타이핑해 주세요.")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("getChatAnswer", "질문 입력")
                .addActionRows(ActionRow.of(id))
                .build();

        event.replyModal(modal).queue();

    }

    // 특정 날짜에 잔디를 심은 그룹원 목록 출력
    public static void showDidCommitSomeday(ButtonInteractionEvent event) {

        event.getMessage().delete().queue(); // 메뉴+오리지널 메세지 지우기

        TextInput date = TextInput.create("showDidCommitSomedayText", "잔디체커", TextInputStyle.SHORT)
                .setMinLength(8)
                .setMaxLength(8)
                .setLabel("조회할 날짜")
                .setPlaceholder("yyyyMMdd 형식")
                .setRequired(true)
                .build();

        Modal modal = Modal.create("showDidCommitSomeday", "날짜 입력")
                .addActionRows(ActionRow.of(date))
                .build();

        event.replyModal(modal).queue();

    }

}
