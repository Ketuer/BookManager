package book.manager.panel;

import dandelion.ui.component.*;
import dandelion.ui.lang.i18n;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class IndexPanel extends DPanel {

    String language = i18n.getDefaultLanguage();

    DTextArea textArea = new DTextArea(0, 0);

    public IndexPanel() {
        super("panel.title.index");

        try {
            DImage banner = new DImage(450, 250,
                    ImageIO.read(new URL("http://www.cduestc.cn/attachment/core/label/2021_03/08_09/1838541d308aafa0.jpg")));
            banner.setArc(15);
            this.add(banner, 10, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        textArea.setText(i18n.format("index.rule", language));
        textArea.setEditable(false);
        DScroll textScroll = new DScroll(300, 250, textArea);
        this.add(textScroll, 470, 10);

        DPanel hot = new DPanel("", 760, 165);
        DLabel title = new DLabel("label.hot");
        title.setFont(new Font("", Font.PLAIN, 14));
        DScroll scroll = new DScroll(740, 135, this.createBookshelf());
        hot.add(scroll, 10, 25);
        hot.add(title, 360, 5);
        this.add(hot, 10, 270);
    }

    @Override
    public void switchLanguage(String language) {
        super.switchLanguage(language);
        textArea.setText(i18n.format("index.rule", language));
        switch (language){
            case "zh_cn":
                textArea.setPreferredSize(new Dimension(275, 430));
                break;
            case "en_us":
                textArea.setPreferredSize(new Dimension(275, 850));
                break;
        }
    }

    private DPanel createBookshelf(){
        DPanel panel = new DPanel("");
        List<String> arr = Arrays.asList(
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3718100000,193313394&fm=224&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3880210791,131443989&fm=224&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=69977264,83632252&fm=224&gp=0.jpg",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2083154963,2889519332&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2350319676,2448444243&fm=26&gp=0.jpg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=996358702,1569636658&fm=15&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2617562981,693058518&fm=224&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1522366973,711223512&fm=26&gp=0.jpg");
        for (int i = 0; i < arr.size(); i++) {
            String str = arr.get(i);
            try {
                DImage image = new DImage(80, 100, ImageIO.read(new URL(str)));
                panel.add(image, 10 + 100*i, 10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(arr.size() * 100 + 20, 110));
        return panel;
    }
}
