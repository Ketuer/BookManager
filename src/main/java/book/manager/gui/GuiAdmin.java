package book.manager.gui;

import book.manager.config.ClientConfig;
import book.manager.entity.Account;
import book.manager.panel.*;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.DIcon;
import dandelion.ui.component.DTab;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Loading;

public class GuiAdmin extends GuiMain{

    Account account;

    public GuiAdmin(Account account, boolean save){
        this.account = account;
        if(save) {
            ClientConfig.put("savePassword", true);
            ClientConfig.put("password", account.getPassword());
            ClientConfig.put("userName", account.getName());
        }
    }

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

        loading.updateState(new Text("load.admin.account"), 50);
        DIcon icon_tab_account = new DIcon("/light/tab_user.png", DIcon.JAR);
        icon_tab_account.registerColorConfig(ColorSwitch.DARK, "/dark/tab_user.png", DIcon.JAR);
        tab.add(new UserPanel(this));
        tab.setIconAt(3, icon_tab_account);

        loading.updateState(new Text("load.admin.setting"), 60);
        DIcon icon_tab_setting = new DIcon("/light/tab_setting.png", DIcon.JAR);
        icon_tab_setting.registerColorConfig(ColorSwitch.DARK, "/dark/tab_setting.png", DIcon.JAR);
        tab.add(new SettingPanel(this,account));
        tab.setIconAt(4, icon_tab_setting);

        this.add(tab, 0, 0);
        return true;
    }
}
