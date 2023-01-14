package jda.interaction;

import jda.JDAController;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class SlashMenus {

    public static List<CommandData> getMenusList() {

        OptionType str = OptionType.STRING;

        List<CommandData> cmdList = new ArrayList<>();
        cmdList.add(Commands.slash(JDAController.CMD_ME, "내 잔디 정보를 조회합니다."));
        cmdList.add(Commands.slash(JDAController.CMD_JANDIYA, "잔디의 친구 ChatGPT AI에게 질문을 해봅니다.")
                .addOption(str, "option", "질문을 입력해 주세요.", true)
        );
        cmdList.add(Commands.slash(JDAController.CMD_NAME, "그룹원의 이름을 입력하여 그룹원의 잔디 정보를 조회합니다.")
                .addOption(str, "option", "그룹원의 이름을 입력해 주세요.", true)
        );
        cmdList.add(Commands.slash(JDAController.CMD_ID, "특정 ID의 잔디 정보를 조회합니다. 그룹원이 아닌 사람도 조회 가능합니다.")
                .addOption(str, "option", "조회하고자 하는 사람의 id를 입력하세요.", true)
        );
        cmdList.add(Commands.slash(JDAController.CMD_LIST_YESTERDAY_SUCCESS, "어제 잔디를 심는데 성공한 그룹원 목록을 확인합니다."));
        cmdList.add(Commands.slash(JDAController.CMD_LIST_YESTERDAY_FAIL, "어제 잔디 심기를 깜박한 그룹원 목록을 확인합니다."));
        cmdList.add(Commands.slash(JDAController.CMD_LIST_TODAY_SUCCESS, "오늘 잔디를 심는데 성공한 그룹원 목록을 확인합니다."));
        cmdList.add(Commands.slash(JDAController.CMD_LIST_BY_DATE, "특정 날짜에 잔디를 심는데 성공한 그룹원 목록을 확인합니다.")
                .addOption(str, "option", "날짜를 입력하세요. yyyyMMdd 형태로 입력하셔야 합니다.", true)
        );
        cmdList.add(Commands.slash(JDAController.CMD_ABOUT, "이 봇에 대한 소개와 도움말을 출력합니다."));
        return cmdList;

    }
}
