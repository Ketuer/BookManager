package book.manager.panel;

import book.manager.config.ClientConfig;
import book.manager.entity.Account;
import dandelion.ui.color.ColorConfig;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.*;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.URL;

public class SettingPanel extends DPanel {

    Gui gui;

    public SettingPanel(Gui gui, Account account) {
        super("panel.title.setting");
        this.gui = gui;

        try {
            DImage image = new DImage(130, 130,
                    ImageIO.read(new URL(account.getHeader())));
            image.setArc(130);
            this.add(image, 325, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DLabel name = new DLabel(account.getRole().equals("Admin") ?
                new Text("label.admin.name", account.getName()) :
                new Text("label.reader.name", account.getName()), 0);
        name.setFont(new Font("", Font.PLAIN, 16));
        this.add(name, (780 - name.getWidth())/2, 170);
        DLabel note = new DLabel(account.getNote());
        this.add(note, (780 - note.getWidth())/2, 200);


        DLabel languageSetting = new DLabel("setting.language");
        languageSetting.setFont(new Font("", Font.PLAIN, 15));
        this.add(languageSetting, 20, 290);

        DSelect<Text> languageSelect =
                new DSelect<>(200, 25, new Text("setting.language.zh_cn"), new Text("setting.language.en_us"));
        this.add(languageSelect, 20, 315);
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
        this.add(colorSetting, 20, 360);
        DSelect<Text> colorSelect =
                new DSelect<>(200, 25, new Text("setting.color.light"), new Text("setting.color.dark"));
        colorSelect.setSelectedIndex(ClientConfig.getInt("themeColorSetting"));
        this.add(colorSelect, 20, 385);
        colorSelect.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                gui.switchColor(convertColor(e.getItem().toString()));
                ClientConfig.put("themeColor", e.getItem().toString());
                ClientConfig.put("themeColorSetting", colorSelect.getSelectedIndex());
            }
        });

        DLabel version = new DLabel(new Text("label.version", "1.0.0 Beta"), 0);
        DButton update = new DButton("button.update", 100, 30,
                e -> gui.showConfirmTip("tip.update", "tip.button.ok", 200, 150));
        this.add(update, 640, 400);
        this.add(version, 630, 370);
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
