package book.manager.panel;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.BookMapper;
import book.manager.entity.Borrow;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.*;
import dandelion.ui.lang.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowPanel extends DPanel {

    List<Borrow> list = new ArrayList<>();

    DTable table = new DTable();
    DScroll borrowScroll = new DScroll(760, 390, table);
    DIcon searchIcon = new DIcon("/light/input_search.png", DIcon.JAR);
    DTextField search = new DTextField(searchIcon, 390, 25, "hint.search");
    DSelect<Text> order = new DSelect<>(190, 25, new Text("order.start"), new Text("order.end"));
    DButton addBook = new DButton("button.borrow.add", 80, 25);
    DButton remBook = new DButton("button.borrow.remove", 80, 25);

    public BorrowPanel() {
        super("panel.title.borrow");
        this.updateList();

        this.add(search, 10, 10);
        this.add(addBook, 605, 10);
        this.add(remBook, 690, 10);
        searchIcon.registerColorConfig(ColorSwitch.DARK, "/dark/input_search.png", DIcon.JAR);
        table.setFont(new Font("", Font.PLAIN, 14));
        table.setRowHeight(25);
        this.add(borrowScroll, 10, 45);
        this.add(order, 405, 10);
    }

    /**
     * 更新借阅列表
     */
    private void updateList(){
        list.clear();
        BookMapper mapper = DatabaseManager.getBookMapper();
        list.addAll(mapper.getAllLendInfo());
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
