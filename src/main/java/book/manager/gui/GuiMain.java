package book.manager.gui;

import book.manager.dao.DatabaseManager;
import book.manager.panel.ReaderPanel;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.DButton;
import dandelion.ui.component.DIcon;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Loading;
import org.apache.log4j.Logger;

import java.awt.*;

public abstract class GuiMain extends Gui {
    Logger logger = Logger.getLogger(GuiMain.class);

    public GuiMain(){
        super(new Text("gui.title.main"), 800, 500, true);
    }

    @Override
    protected boolean onLoad(Loading loading) {
        loading.updateState(new Text("load.main"), 10);
        DButton exit = new DButton("button.title.exit", 110, 25, e -> close());
        exit.registerColorConfig(ColorSwitch.LIGHT, new DButton.ButtonColorConfig(new Color(243, 213, 213),
                new Color(231, 231, 231), new Color(239, 190, 190),
                Color.black, new Color(227, 149, 149)));
        exit.registerColorConfig(ColorSwitch.DARK, new DButton.ButtonColorConfig(new Color(163, 57, 57),
                new Color(147, 147, 147), new Color(212, 32, 32),
                new Color(230, 230, 230), new Color(231, 122, 122)));
        DIcon icon_exit = new DIcon("/light/button_exit.png", DIcon.JAR);
        icon_exit.registerColorConfig(ColorSwitch.DARK, "/dark/button_exit.png", DIcon.JAR);
        exit.setIcon(icon_exit);
        this.add(exit, 685, 10);
        return true;
    }

    @Override
    protected boolean onClose() {
        DButton ok = new DButton("tip.button.ok");
        DButton.ButtonColorConfig config = ok.getColorConfig(ColorSwitch.LIGHT);
        config.backgroundColor = new Color(81, 168, 238);
        config.fontColor = Color.white;
        ok.getColorConfig(ColorSwitch.DARK).backgroundColor = new Color(61, 141, 205);
        DButton cancel = new DButton("tip.button.cancel");
        boolean exit = this.showSelectTip("tip.exit", 250, 150, ok, cancel) == 0;
        if(exit) logger.info("正在退出客户端...");
        return exit;
    }
}
