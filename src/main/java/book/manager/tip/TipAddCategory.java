package book.manager.tip;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.BookMapper;
import book.manager.entity.Category;
import dandelion.ui.component.DButton;
import dandelion.ui.component.DLabel;
import dandelion.ui.component.DTextField;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.LanguageSwitch;
import dandelion.ui.tip.Tip;
import dandelion.ui.tip.TipConfirm;

import java.awt.*;

public class TipAddCategory extends Tip implements LanguageSwitch {

    private boolean isSuccess = false;
    DButton cancel = new DButton("tip.button.cancel", 100, 30, e -> close());
    DButton ok = new DButton("tip.button.ok", 100, 30, e -> {
        if(insert()) {
            close();
            isSuccess = true;
        }
    });

    DTextField id = new DTextField(null, 200, 30, "hint.category.id");
    DTextField name = new DTextField(null, 200, 30, "hint.category.name");
    Gui gui;

    public TipAddCategory(Gui parent) {
        super(parent, 300, 130, true);
        this.gui = parent;

        this.addComponent(new DLabel("label.type.id"), 10, 15);
        this.addComponent(id, 70, 10);

        this.addComponent(new DLabel("label.type.name"), 10, 55);
        this.addComponent(name, 70, 50);

        this.addComponent(ok, 45, 90);
        this.addComponent(cancel, 155, 90);
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
        String id = this.id.getText();
        String name = this.name.getText();

        if(id.isEmpty() || name.isEmpty()){
            TipConfirm confirm = new TipConfirm(gui, "tip.edit.finish", "tip.button.ok", 200, 150);
            confirm.setAlwaysOnTop(true);
            confirm.display();
            return false;
        }else {
            Category category = new Category(id, name);
            BookMapper mapper = DatabaseManager.getBookMapper();
            mapper.addCategory(category);
            return true;
        }
    }
}
