package book.manager.panel;

import book.manager.config.ClientConfig;
import dandelion.ui.color.ColorConfig;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.DLabel;
import dandelion.ui.component.DPanel;
import dandelion.ui.component.DSelect;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;

import java.awt.*;
import java.awt.event.ItemEvent;

public class SettingPanel extends DPanel {

    Gui gui;

    public SettingPanel(Gui gui) {
        super("panel.title.setting");
        this.gui = gui;

        DLabel languageSetting = new DLabel("setting.language");
        languageSetting.setFont(new Font("", Font.PLAIN, 15));
        this.add(languageSetting, 20, 10);

        DSelect<Text> languageSelect =
                new DSelect<>(200, 25, new Text("setting.language.zh_cn"), new Text("setting.language.en_us"));
        this.add(languageSelect, 20, 35);
        languageSelect.setSelectedIndex(ClientConfig.getInt("languageSetting"));
        languageSelect.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                gui.switchLanguage(convertLanguage(e.getItem().toString()));
                ClientConfig.put("language", e.getItem().toString());
                ClientConfig.put("languageSetting", languageSelect.getSelectedIndex());
            }
        });

        DLabel colorSetting = new DLabel("setting.color");
        colorSetting.setFont(new Font("", Font.PLAIN, 15));
        this.add(colorSetting, 20, 80);
        DSelect<Text> colorSelect =
                new DSelect<>(200, 25, new Text("setting.color.light"), new Text("setting.color.dark"));
        colorSelect.setSelectedIndex(ClientConfig.getInt("themeColorSetting"));
        this.add(colorSelect, 20, 105);
        colorSelect.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                gui.switchColor(convertColor(e.getItem().toString()));
                ClientConfig.put("themeColor", e.getItem().toString());
                ClientConfig.put("themeColorSetting", colorSelect.getSelectedIndex());
            }
        });
    }

    public static String convertLanguage(String s){
        switch (s){
            default:
            case "setting.language.zh_cn":
                return "zh_cn";
            case "setting.language.en_us":
                return "en_us";
        }
    }

    public static ColorConfig convertColor(String s){
        switch (s){
            default:
            case "setting.color.light":
                return ColorSwitch.LIGHT;
            case "setting.color.dark":
                return ColorSwitch.DARK;
        }
    }
}
