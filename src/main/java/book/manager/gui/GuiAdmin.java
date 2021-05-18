package book.manager.gui;

import book.manager.panel.BookPanel;
import book.manager.panel.SettingPanel;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.DIcon;
import dandelion.ui.component.DPanel;
import dandelion.ui.component.DTab;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Loading;

public class GuiAdmin extends GuiMain{

    @Override
    protected boolean onLoad(Loading loading) {
        super.onLoad(loading);
        loading.updateState(new Text("load.admin.index"), 20);
        DTab tab = new DTab(this);
        tab.setSize(super.getWidth(), super.getHeight());
        DPanel panel1 = new DPanel("首页");
        tab.add(panel1);

        loading.updateState(new Text("load.admin.book"), 30);
        DIcon icon_tab_book = new DIcon("/light/tab_book.png", DIcon.JAR);
        icon_tab_book.registerColorConfig(ColorSwitch.DARK, "/dark/tab_book.png", DIcon.JAR);
        tab.add(new BookPanel());
        tab.setIconAt(1, icon_tab_book);

        loading.updateState(new Text("load.admin.lend"), 40);
        DPanel panel3 = new DPanel("借阅管理");
        tab.add(panel3);

        loading.updateState(new Text("load.admin.setting"), 50);
        DIcon icon_tab_setting = new DIcon("/light/tab_setting.png", DIcon.JAR);
        icon_tab_setting.registerColorConfig(ColorSwitch.DARK, "/dark/tab_setting.png", DIcon.JAR);
        tab.add(new SettingPanel(this));
        tab.setIconAt(3, icon_tab_setting);

        this.add(tab, 0, 0);
        return true;
    }
}
