package book.manager.gui;

import book.manager.config.ClientConfig;
import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.UserMapper;
import book.manager.entity.Account;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.*;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Loading;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

public class GuiLogin extends Gui {

    Logger logger = Logger.getLogger(GuiLogin.class);

    public GuiLogin() {
        super(new Text("gui.title.login"), 400, 250, true);
        DPanel mainPanel = new DPanel("", 400, 250);
        mainPanel.setOpaque(true);
        mainPanel.setPaintBackground(false);
        this.setContentPane(mainPanel);
        this.setIcon();
    }

    @Override
    protected boolean onLoad(Loading loading) {

        loading.updateState(new Text("load.login.download"), 10);

        UserMapper mapper = DatabaseManager.getUserMapper();

        DImage background = null;
        try {
            if(getColorConfig().equals(ColorSwitch.DARK)){
                background = new DImage(400, 250,
                        ImageIO.read(new URL("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2185970953,3518747076&fm=26&gp=0.jpg")));
            }else {
                background = new DImage(400, 250,
                        ImageIO.read(new URL("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2879430259,1145403574&fm=26&gp=0.jpg")));
            }
            background.setArc(20);
            background.setLocation(0, 0);
            this.add(background);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loading.updateState(new Text("load.login.open"), 20);

        DLabel title = new DLabel("label.login.title");
        title.registerColorConfig(ColorSwitch.LIGHT, new DLabel.LabelColorConfig(Color.gray));
        title.setFont(new Font("", Font.BOLD, 25));
        this.add(title, (getWidth() - title.getWidth())/2, 10);
        DLabel user = new DLabel("label.login.account");
        user.registerColorConfig(ColorSwitch.LIGHT, new DLabel.LabelColorConfig(Color.gray));
        user.setFont(new Font("", Font.PLAIN, 18));
        this.add(user, 50, 105);
        DTextField userName = new DTextField(null, 200, 30, "hint.login.name");
        DLabel pwd = new DLabel("label.login.password");
        pwd.registerColorConfig(ColorSwitch.LIGHT, new DLabel.LabelColorConfig(Color.gray));
        pwd.setFont(new Font("", Font.PLAIN, 18));
        this.add(pwd, 50, 145);
        DPassword password = new DPassword(null, 200, 30, "hint.login.password");
        this.add(userName, 150, 100);
        this.add(password, 150, 140);

        DCheck check = new DCheck(DCheck.RECTANGLE, new Text("check.account"), 18);
        this.add(check, 250, 185);

        if(ClientConfig.getBoolean("savePassword")){
            check.setSelected(true);
            userName.setText(ClientConfig.getString("userName"));
            password.setText(ClientConfig.getString("password"));
        }

        DButton button = new DButton("button.login", 100, 30, e -> {
            Account account = mapper.getAccount(userName.getText(), password.getText());
            if(account == null) {
                showConfirmTip("tip.login.failed", "tip.button.ok", 200, 150);
                return;
            }
            logger.info("?????????????????????????????????"+account.getName()+" ("+account.getRole()+")");
            if(account.getRole().equals("Admin")){
                this.redirectGui(new GuiAdmin(account, check.isSelected()));
            }else{
                this.redirectGui(new GuiReader(account, check.isSelected()));
            }
        });
        this.add(button, 95, 210);
        this.add(new DButton("button.exit", 100, 30, e -> this.close()), 205, 210);
        this.getContentPane().setComponentZOrder(background, this.getContentPane().getComponentCount() - 1);
        return true;
    }

    /**
     * ?????????????????????????????????????????????
     * MAC OS X????????????dock??????????????????????????????
     */
    private void setIcon(){
        DIcon icon = new DIcon("/icon.png", DIcon.JAR);
        DIcon icon_big = new DIcon("/icon_big.png", DIcon.JAR);

        try{
            String osName = System.getProperty("os.name");
            if(osName.equals("Mac OS X")){
                Class<?> application = Class.forName("com.apple.eawt.Application");
                Method getApplication = application.getMethod("getApplication");
                Object object = getApplication.invoke(null);
                Method setDockIconImage = application.getMethod("setDockIconImage", Image.class);
                setDockIconImage.invoke(object, icon.getImage());
            }else {
                this.setIconImage(icon_big.getImage());
            }
        }catch (ReflectiveOperationException e){
            e.printStackTrace();
        }
    }
}
