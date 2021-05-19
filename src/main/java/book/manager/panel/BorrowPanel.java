package book.manager.panel;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.BookMapper;
import book.manager.entity.Borrow;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.*;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowPanel extends DPanel {

    List<Borrow> list = new ArrayList<>();

    DTable table = new DTable();
    DScroll borrowScroll = new DScroll(760, 390, table);
    DIcon searchIcon = new DIcon("/light/input_search.png", DIcon.JAR);
    DTextField search = new DTextField(searchIcon, 390, 25, "hint.search");
    String searchKeyword = search.getText();

    DSelect<Text> order = new DSelect<>(190, 25, new Text("order.start"), new Text("order.end"));
    DButton remBook = new DButton("button.borrow.remove", 165, 25);

    Gui gui;
    public BorrowPanel(Gui gui) {
        super("panel.title.borrow");
        this.gui = gui;
        this.updateList();
        this.init();
    }

    /**
     * 初始化界面
     */
    private void init(){
        BookMapper mapper = DatabaseManager.getBookMapper();
        this.add(search, 10, 10);
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                searchKeyword = search.getText();
                updateList();
            }
        });
        this.add(remBook, 605, 10);
        remBook.addActionListener(e -> {
            int row = table.getSelectedRow();
            String aid = table.getValueAt(row, 3).toString();
            String bid = table.getValueAt(row, 4).toString();
            mapper.removeBorrow(aid, bid);
            gui.showConfirmTip("分类删除成功！", "tip.button.ok", 200, 150);
            this.updateList();
        });
        searchIcon.registerColorConfig(ColorSwitch.DARK, "/dark/input_search.png", DIcon.JAR);
        table.setFont(new Font("", Font.PLAIN, 14));
        table.setRowHeight(25);
        this.add(borrowScroll, 10, 45);
        this.add(order, 405, 10);
        order.addItemListener(e -> this.updateList());
    }

    /**
     * 更新借阅列表
     */
    private void updateList(){
        list.clear();
        BookMapper mapper = DatabaseManager.getBookMapper();
        list.addAll(
                mapper.getAllLendInfo()
                        .stream()
                        .sorted((a, b) -> {
                            String orderRule = order.getSelectedItem().toString();
                            Date da, db;
                            if(orderRule.equals("order.start")){
                                da = a.getStart();
                                db = b.getStart();
                            }else {
                                da = a.getEnd();
                                db = b.getEnd();
                            }
                            return da.compareTo(db);
                        })
                        .filter(borrow -> borrow.getTitle().contains(searchKeyword) || borrow.getName().contains(searchKeyword) ||
                                borrow.getAuthor().contains(searchKeyword) || borrow.getDesc().contains(searchKeyword))
                        .collect(Collectors.toList()));
        Object[] title = {"table.book.no", "table.borrow.start", "table.borrow.end",
                "table.borrow.name", "table.book.title", "table.book.author", "table.book.desc"};
        Object[][] data = new Object[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            Borrow borrow = list.get(i);
            data[i][0] = i;
            data[i][1] = borrow.getStart();
            data[i][2] = borrow.getEnd();
            data[i][3] = borrow.getName();
            data[i][4] = borrow.getTitle();
            data[i][5] = borrow.getAuthor();
            data[i][6] = borrow.getDesc();
        }
        table.setData(data, title);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(6).setPreferredWidth(200);
    }
}
