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

        cmdList.add(Commands.slash(JDAController.instance.getCmdMe(), pr.l("menu_desc_me")));
        cmdList.add(Commands.slash(JDAController.instance.getCmdJandiya(), pr.l("menu_desc_heyJandi"))
                .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_question"), true));

        cmdList.add(Commands.slash(JDAController.instance.getCmdListYesterdaySucceed(), pr.l("menu_desc_listYesterdaySucceed")));
        cmdList.add(Commands.slash(JDAController.instance.getCmdListTodaySucceed(), pr.l("menu_desc_listTodaySucceed")));

        cmdList.add(Commands.slash(JDAController.instance.getCmdName(), pr.l("menu_desc_mapByName"))
                .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_name"), true));
        cmdList.add(Commands.slash(JDAController.instance.getCmdId(), pr.l("menu_desc_mapByID"))
                .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_GitHubID"), true));

        cmdList.add(Commands.slash(JDAController.instance.getCmdListYesterdayFail(), pr.l("menu_desc_listYesterdayFail")));
        cmdList.add(Commands.slash(JDAController.instance.getCmdListByDate(), pr.l("menu_desc_listByDate"))
                .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_date"), true));

        if (!TranslationService.mainLanguageLong.equals("English")) {
            cmdList.add(Commands.slash(JDAController.instance.getCmdTranslateEnToMain(), pr.l("menu_desc_ENToMain"))
                        .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_EngLang"), true));
            cmdList.add(Commands.slash(JDAController.instance.getCmdTranslateMainToEn(), pr.l("menu_desc_MainToEN"))
                    .addOption(OptionType.STRING, "option", pr.l("menu_pleaseInput_MainLang"), true));
        }

        cmdList.add(Commands.slash(JDAController.instance.getCmdAbout(), pr.l("menu_desc_about")));
        return cmdList;

    }
}
