package jda.menu;

import jda.JDAController;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import translate.TranslationService;

import java.util.ArrayList;
import java.util.List;

import static init.Initializer.pr;

public class SlashMenu {

    public static List<CommandData> getMenusList() {

        List<CommandData> cmdList = new ArrayList<>();

        cmdList.add(Commands.slash(JDAController.CMD_ME, pr.l("menu_desc_me")));
        cmdList.add(Commands.slash(JDAController.CMD_JANDIYA, pr.l("menu_desc_heyJandi"))
                .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_question"), true));

        cmdList.add(Commands.slash(JDAController.CMD_LIST_YESTERDAY_SUCCEED, pr.l("menu_desc_listYesterdaySucceed")));
        cmdList.add(Commands.slash(JDAController.CMD_LIST_TODAY_SUCCEED, pr.l("menu_desc_listTodaySucceed")));

        cmdList.add(Commands.slash(JDAController.CMD_NAME, pr.l("menu_desc_mapByName"))
                .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_name"), true));
        cmdList.add(Commands.slash(JDAController.CMD_ID, pr.l("menu_desc_mapByID"))
                .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_GitHubID"), true));

        cmdList.add(Commands.slash(JDAController.CMD_LIST_YESTERDAY_FAIL, pr.l("menu_desc_listYesterdayFail")));
        cmdList.add(Commands.slash(JDAController.CMD_LIST_BY_DATE, pr.l("menu_desc_listByDate"))
                .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_date"), true));

        if (!TranslationService.mainLanguageLong.equals("English")) {
            cmdList.add(Commands.slash(JDAController.CMD_TRANSLATE_EN_TO_MAIN, pr.l("menu_desc_ENToMain"))
                        .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_EngLang"), true));
            cmdList.add(Commands.slash(JDAController.CMD_TRANSLATE_MAIN_TO_EN, pr.l("menu_desc_MainToEN"))
                    .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_MainLang"), true));
        }

        cmdList.add(Commands.slash(JDAController.CMD_ABOUT, pr.l("menu_desc_about")));
        return cmdList;

    }
}
