package book.manager.panel;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.UserMapper;
import book.manager.entity.Account;
import book.manager.tip.TipAddAccount;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.*;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserPanel extends DPanel {

    DIcon searchIcon = new DIcon("/light/input_search.png", DIcon.JAR);
    DTextField search = new DTextField(searchIcon, 180, 25, "hint.search.user");
    DTable table = new DTable();
    DScroll scroll = new DScroll(360, 370, table);
    DPanel userInfo = new DPanel("", 360, 400);
    List<Account> accounts = new ArrayList<>();
    DTextField name = new DTextField(null, 200, 30, "hint.account.name");
    DSelect<Text> sex = new DSelect<>(100, 25, new Text("select.male"), new Text("select.female"));
    DTextArea note = new DTextArea(200, 100, "hint.account.note");

    String searchKeyword = search.getText();

    Gui gui;

    public UserPanel(Gui gui){
        super("panel.title.account");

        this.gui = gui;

        updateList();
        updateScroll();
        selectDefault();

        searchIcon.registerColorConfig(ColorSwitch.DARK, "/dark/input_search.png", DIcon.JAR);

        this.add(scroll, 20, 50);
        table.setFont(new Font("", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) selectAccount(accounts.get(row));
        });
        this.add(userInfo, 400, 20);

        this.add(search, 20, 20);
        this.add(new DButton("button.account.add", 80, 25, e -> createUser()), 210, 20);
        this.add(new DButton("button.account.remove", 80, 25, e -> removeUser()), 300, 20);

        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                searchKeyword = search.getText();
                updateScroll();
            }
        });
    }

    private void updateUser(){
        String name = this.name.getText();
        String sex = this.sex.getSelectedItem().toString().equals("select.male") ? "男" : "女";
        String note = this.note.getText();

        UserMapper mapper = DatabaseManager.getUserMapper();
        int row = table.getSelectedRow();
        if(row >= 0){
            String oldName = table.getValueAt(row, 1).toString();
            Account account = accounts.get(row);
            account.setName(name);
            account.setSex(sex);
            account.setNote(note);
            mapper.updateUser(account, oldName);
            gui.showConfirmTip("tip.account.update", "tip.button.ok", 200, 150);
            updateList();
            updateScroll();
            selectDefault();
        }
    }

    private void removeUser(){
        UserMapper mapper = DatabaseManager.getUserMapper();
        int row = table.getSelectedRow();
        if(row >= 0){
            String name = table.getValueAt(row, 1).toString();
            mapper.removeUser(name);
            gui.showConfirmTip("tip.account.delete", "tip.button.ok", 200, 150);
            updateList();
            updateScroll();
            selectDefault();
        }
    }

    private void createUser(){
        TipAddAccount tipAddAccount = new TipAddAccount(gui);
        boolean success = tipAddAccount.getResult();
        if(success)
            gui.showConfirmTip("tip.account.add", "tip.button.ok", 200, 150);
        updateList();
        updateScroll();
        selectDefault();
    }

    /**
     * 更新读者列表
     */
    private void updateList(){
        accounts.clear();
        UserMapper mapper = DatabaseManager.getUserMapper();
        accounts.addAll(mapper.getReaders());
    }

    /**
     * 更新滚动列表和Table
     */
    private void updateScroll(){
        List<Account> list = accounts
                .stream()
                .filter(account -> account.getName().contains(searchKeyword) || account.getNote().contains(searchKeyword))
                .collect(Collectors.toList());
        Object[] title = {"table.book.no", "table.account.name", "table.account.sex", "table.account.note"};
        Object[][] data = new Object[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            Account account = list.get(i);
            data[i][0] = i;
            data[i][1] = account.getName();
            data[i][2] = account.getSex();
            data[i][3] = account.getNote();
        }
        table.setData(data, title);
    }

    private void selectDefault(){
        if(accounts.size() > 0){
            this.selectAccount(accounts.get(0));
            table.setRowSelectionInterval(0, 0);
        }
    }

    private void selectAccount(Account account){
        userInfo.removeAll();
        try {
            DImage image = new DImage(130, 130, ImageIO.read(new URL(account.getHeader())));
            image.setArc(130);
            userInfo.add(image, 115, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }

        name.setText(account.getName());
        sex.setSelectedIndex(account.getSex().equals("男") ? 0 : 1);
        note.setText(account.getNote());

        userInfo.add(new DLabel("label.account.name"), 30, 175);
        userInfo.add(name, 100, 170);
        userInfo.add(new DLabel("label.account.sex"), 30, 215);
        userInfo.add(sex, 100, 210);
        userInfo.add(new DLabel("label.account.note"), 30, 245);
        userInfo.add(note, 100, 245);

        userInfo.add(new DButton("button.account.save", 100, 30, e -> updateUser()), 130, 360);

        userInfo.repaint();
    }
}
