package book.manager.tip;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.UserMapper;
import book.manager.entity.Account;
import dandelion.ui.component.*;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.LanguageSwitch;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Tip;
import dandelion.ui.tip.TipConfirm;

import java.awt.*;

public class TipAddAccount extends Tip implements LanguageSwitch {

    private boolean isSuccess = false;

    DButton cancel = new DButton("tip.button.cancel", 100, 30, e -> close());
    DButton ok = new DButton("tip.button.ok", 100, 30, e -> {
        if(insert()) {
            close();
            isSuccess = true;
        }
    });

    DTextField name = new DTextField(null, 200, 30, "hint.account.name");
    DPassword password = new DPassword(null, 200, 30, "hint.account.password");
    DTextField header = new DTextField(null, 200, 30, "hint.account.header");
    DTextArea note = new DTextArea(380, 70, "hint.account.note");
    DSelect<Object> sex;
    Gui gui;

    public TipAddAccount(Gui parent) {
        super(parent, 400, 300, true);

        this.gui = parent;

        this.addComponent(ok, 95, 260);
        this.addComponent(cancel, 205, 260);

        this.addComponent(new DLabel("label.account.name"), 10, 15);
        this.addComponent(name, 90, 10);
        this.addComponent(new DLabel("label.login.password"), 10, 55);
        this.addComponent(password, 90, 50);
        this.addComponent(new DLabel("label.account.header"), 10, 95);
        this.addComponent(header, 90, 90);

        this.addComponent(new DLabel("label.account.note"), 155);
        this.addComponent(note, 175);

        this.addComponent(new DLabel("label.account.sex"), 10, 130);
        sex = new DSelect<>(150, 25, new Text("select.male"), new Text("select.female"));
        this.addComponent(sex, 90, 125);

        this.switchColor(parent.getColorConfig());
        this.switchLanguage(parent.getLanguage());
    }

    @Override
    public void switchLanguage(String language) {
        for(Component c : this.getContentPane().getComponents()){
            if(c instanceof LanguageSwitch) ((LanguageSwitch) c).switchLanguage(language);
        }
    }

    public boolean getResult(){
        this.display();
        return isSuccess;
    }

    private boolean insert(){
        String name = this.name.getText();
        String password = this.password.getText();
        String header = this.header.getText();
        String note = this.note.getText();
        String sex = this.sex.getSelectedItem().toString().equals("select.male") ? "男" : "女";

        if(name.isEmpty() || header.isEmpty() || note.isEmpty() || password.isEmpty()){
            TipConfirm confirm = new TipConfirm(gui, "tip.edit.finish", "tip.button.ok", 200, 150);
            confirm.setAlwaysOnTop(true);
            confirm.display();
            return false;
        }else{
            Account account = new Account(0, name, sex, "Reader", header, password, note);
            UserMapper mapper = DatabaseManager.getUserMapper();
            mapper.createUser(account);
            return true;
        }
    }

}
