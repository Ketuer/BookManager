package book.manager.gui;

import book.manager.config.ClientConfig;
import book.manager.entity.Account;
import book.manager.panel.*;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.DIcon;
import dandelion.ui.component.DTab;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Loading;
import org.apache.log4j.Logger;

public class GuiReader extends GuiMain{

    Logger logger = Logger.getLogger(GuiReader.class);

    Account account;

    public GuiReader(Account account, boolean save){
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

        logger.info("开始加载读者界面...");

        DTab tab = new DTab(this);
        tab.setSize(super.getWidth(), super.getHeight());

        logger.info("正在加载首页信息...");
        loading.updateState(new Text("load.admin.index"), 20);
        DIcon icon_tab_home = new DIcon("/light/tab_home.png", DIcon.JAR);
        icon_tab_home.registerColorConfig(ColorSwitch.DARK, "/dark/tab_home.png", DIcon.JAR);
        tab.add(new IndexPanel());
        tab.setIconAt(0, icon_tab_home);

        logger.info("正在加载借阅...");
        loading.updateState(new Text("load.admin.book"), 30);
        DIcon icon_tab_book = new DIcon("/light/tab_reader.png", DIcon.JAR);
        icon_tab_book.registerColorConfig(ColorSwitch.DARK, "/dark/tab_reader.png", DIcon.JAR);
        tab.add(new ReaderPanel(account, this));
        tab.setIconAt(1, icon_tab_book);

        logger.info("开始加载设置界面...");
        loading.updateState(new Text("load.admin.setting"), 50);
        DIcon icon_tab_setting = new DIcon("/light/tab_setting.png", DIcon.JAR);
        icon_tab_setting.registerColorConfig(ColorSwitch.DARK, "/dark/tab_setting.png", DIcon.JAR);
        tab.add(new SettingPanel(this, account));
        tab.setIconAt(2, icon_tab_setting);

        this.add(tab, 0, 0);
        return true;
    }
}
