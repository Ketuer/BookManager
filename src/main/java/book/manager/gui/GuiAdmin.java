package book.manager.gui;

import book.manager.panel.BookPanel;
import book.manager.panel.BorrowPanel;
import book.manager.panel.IndexPanel;
import book.manager.panel.SettingPanel;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.DIcon;
import dandelion.ui.component.DTab;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Loading;

public class GuiAdmin extends GuiMain{

    @Override
    protected boolean onLoad(Loading loading) {
        super.onLoad(loading);
        DTab tab = new DTab(this);
        tab.setSize(super.getWidth(), super.getHeight());

        loading.updateState(new Text("load.admin.index"), 20);
        DIcon icon_tab_home = new DIcon("/light/tab_home.png", DIcon.JAR);
        icon_tab_home.registerColorConfig(ColorSwitch.DARK, "/dark/tab_home.png", DIcon.JAR);
        tab.add(new IndexPanel());
        tab.setIconAt(0, icon_tab_home);

        loading.updateState(new Text("load.admin.book"), 30);
        DIcon icon_tab_book = new DIcon("/light/tab_book.png", DIcon.JAR);
        icon_tab_book.registerColorConfig(ColorSwitch.DARK, "/dark/tab_book.png", DIcon.JAR);
        tab.add(new BookPanel(this));
        tab.setIconAt(1, icon_tab_book);

        loading.updateState(new Text("load.admin.lend"), 40);
        DIcon icon_tab_borrow = new DIcon("/light/tab_borrow.png", DIcon.JAR);
        icon_tab_borrow.registerColorConfig(ColorSwitch.DARK, "/dark/tab_borrow.png", DIcon.JAR);
        tab.add(new BorrowPanel(this));
        tab.setIconAt(2, icon_tab_borrow);

        loading.updateState(new Text("load.admin.setting"), 50);
        DIcon icon_tab_setting = new DIcon("/light/tab_setting.png", DIcon.JAR);
        icon_tab_setting.registerColorConfig(ColorSwitch.DARK, "/dark/tab_setting.png", DIcon.JAR);
        tab.add(new SettingPanel(this));
        tab.setIconAt(3, icon_tab_setting);

        this.add(tab, 0, 0);
        return true;
    }
}
