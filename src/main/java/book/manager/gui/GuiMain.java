package book.manager.gui;

import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.DButton;
import dandelion.ui.component.DIcon;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Loading;

import java.awt.*;

public abstract class GuiMain extends Gui {
    public GuiMain(){
        super(new Text("gui.title.main"), 700, 450, true);
    }

    @Override
    protected boolean onLoad(Loading loading) {
        loading.updateState(new Text("load.main"), 10);
        DButton exit = new DButton("button.title.exit", 110, 25, e -> close());
        exit.registerColorConfig(ColorSwitch.LIGHT, new DButton.ButtonColorConfig(new Color(210, 92, 92),
                new Color(231, 231, 231), new Color(213, 179, 179),
                Color.black, new Color(229, 112, 112)));
        exit.registerColorConfig(ColorSwitch.DARK, new DButton.ButtonColorConfig(new Color(163, 57, 57),
                new Color(147, 147, 147), new Color(212, 32, 32),
                new Color(230, 230, 230), new Color(231, 122, 122)));
        DIcon icon_exit = new DIcon("/light/button_exit.png", DIcon.JAR);
        icon_exit.registerColorConfig(ColorSwitch.DARK, "/dark/button_exit.png", DIcon.JAR);
        exit.setIcon(icon_exit);
        this.add(exit, 585, 10);
        return true;
    }

    @Override
    protected boolean onClose() {
        DButton ok = new DButton("tip.button.ok");
        DButton cancel = new DButton("tip.button.cancel");
        return this.showSelectTip("tip.exit", 200, 100, ok, cancel) == 0;
    }
}
