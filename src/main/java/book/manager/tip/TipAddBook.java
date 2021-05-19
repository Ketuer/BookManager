package book.manager.tip;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.BookMapper;
import book.manager.entity.Book;
import dandelion.ui.component.*;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.LanguageSwitch;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Tip;
import dandelion.ui.tip.TipConfirm;

import java.awt.*;
import java.util.Set;

public class TipAddBook extends Tip implements LanguageSwitch {

    private boolean isSuccess = false;

    DButton cancel = new DButton("tip.button.cancel", 100, 30, e -> close());
    DButton ok = new DButton("tip.button.ok", 100, 30, e -> {
        if(insert()) {
            close();
            isSuccess = true;
        }
    });

    DTextField title = new DTextField(null, 200, 30, "hint.input.title");
    DTextField name = new DTextField(null, 200, 30, "hint.input.author");
    DTextArea desc = new DTextArea(380, 70, "hint.input.desc");
    DSelect<Text> time = new DSelect<>(150, 25,
            new Text("2021"), new Text("2020"), new Text("2019"), new Text("2018"));
    DSelect<Object> type;
    Gui gui;

    public TipAddBook(Gui parent, Set<String> categories) {
        super(parent, 400, 300, true);

        this.gui = parent;

        this.addComponent(ok, 95, 260);
        this.addComponent(cancel, 205, 260);

        this.addComponent(new DLabel("label.title"), 10, 15);
        this.addComponent(title, 70, 10);
        this.addComponent(new DLabel("label.author"), 10, 55);
        this.addComponent(name, 70, 50);
        this.addComponent(new DLabel("label.time"), 10, 95);
        this.addComponent(time, 70, 90);

        this.addComponent(new DLabel("label.desc"), 155);
        this.addComponent(desc, 175);

        this.addComponent(new DLabel("label.type"), 10, 130);
        type = new DSelect<>(150, 25, categories.toArray());
        this.addComponent(type, 70, 125);

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
        String title = this.title.getText();
        String author = this.name.getText();
        String year = this.time.getSelectedItem().toString();
        String desc = this.desc.getText();
        String type = this.type.getSelectedItem().toString();

        if(title.isEmpty() || author.isEmpty() || desc.isEmpty()){
            TipConfirm confirm = new TipConfirm(gui, "tip.edit.finish", "tip.button.ok", 200, 150);
            confirm.setAlwaysOnTop(true);
            confirm.display();
            return false;
        }else{
            Book book = new Book(title, author, desc, Integer.parseInt(year));
            BookMapper mapper = DatabaseManager.getBookMapper();
            mapper.insertBook(book, type);
            return true;
        }
    }
}
