package book.manager;

import book.manager.config.ClientConfig;
import book.manager.dao.DatabaseManager;
import book.manager.gui.GuiAdmin;
import book.manager.gui.GuiReader;
import book.manager.panel.SettingPanel;
import dandelion.ui.color.ColorConfig;
import dandelion.ui.lang.i18n;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger(Main.class);
        logger.info("正在读取本地配置文件...");
        ClientConfig.readLocalSetting();
        i18n.setDefaultLanguage(SettingPanel.convertLanguage(ClientConfig.getString("language")));
        ColorConfig config = SettingPanel.convertColor(ClientConfig.getString("themeColor"));

        logger.info("正在连接数据库...");
        DatabaseManager.init();

        logger.info("正在启动客户端...");
        GuiAdmin login = new GuiAdmin();
        login.switchColor(config);
        login.display();
        logger.info("客户端已启动！");
    }
}
