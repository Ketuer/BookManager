package book.manager.panel;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.BookMapper;
import book.manager.entity.Account;
import book.manager.entity.Book;
import book.manager.entity.Borrow;
import book.manager.gui.GuiReader;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.*;
import dandelion.ui.gui.Gui;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ReaderPanel extends DPanel {

    Logger logger = Logger.getLogger(ReaderPanel.class);
    private final Map<String, List<Book>> books = new HashMap<>();
    private final List<Borrow> borrows = new ArrayList<>();
    DefaultMutableTreeNode all = new DefaultMutableTreeNode("tree.top.book");
    DTree tree = new DTree(all, 170, 370);

    DButton refresh = new DButton("button.func.refresh", 170, 30, e -> this.updateList());

    DTable table = new DTable();
    DTable tableBorrow = new DTable();

    DScroll detailScroll = new DScroll(560, 200, table);
    DScroll borrowScroll = new DScroll(560, 145, tableBorrow);

    DIcon searchIcon = new DIcon("/light/input_search.png", DIcon.JAR);
    DTextField search = new DTextField(searchIcon, 470, 25, "hint.search");
    String searchKeyword = search.getText();
    DButton borrowBook = new DButton("button.book.borrow", 80, 25);

    DButton returnBook = new DButton("button.borrow.return", 100, 30);

    Account account;
    Gui gui;

    public ReaderPanel(Account account, Gui gui) {
        super("panel.title.reader");

        this.gui = gui;

        BookMapper mapper = DatabaseManager.getBookMapper();

        this.account = account;

        this.updateList();
        this.add(tree, 20, 20);
        tree.setFont(new Font("", Font.PLAIN, 14));
        tree.expandRow(0);
        tree.setSelectionRow(1);
        tree.addTreeSelectionListener(e -> this.selectDefault());

        this.add(refresh, 20, 400);

        this.selectDefault();
        table.setFont(new Font("", Font.PLAIN, 14));
        table.setRowHeight(20);
        tableBorrow.setFont(new Font("", Font.PLAIN, 14));
        tableBorrow.setRowHeight(20);
        this.add(detailScroll, 205, 40);
        this.add(borrowScroll, 205, 280);
        DLabel borrowLabel = new DLabel("label.borrow.list");
        borrowLabel.setFont(new Font("", Font.PLAIN, 16));
        this.add(borrowLabel, 205, 255);
        this.add(returnBook, 665, 245);
        searchIcon.registerColorConfig(ColorSwitch.DARK, "/dark/input_search.png", DIcon.JAR);
        this.add(search, 205, 10);
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                searchKeyword = search.getText();
                updateList();
                updateScroll(tree.getSelectionPath().getPath()[1].toString());
            }
        });
        this.add(borrowBook, 685, 10);
        borrowBook.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1) return;
            String title = table.getValueAt(row, 2).toString();
            logger.info("用户借阅了一本图书 "+title);
            mapper.addBorrow(new Borrow(account.getName(), title, "", "", plusDay(0), plusDay(3)));
            gui.showConfirmTip("tip.borrow.ok", "tip.button.ok", 200, 150);
            updateList();
            updateBorrowScroll();
            updateScroll(tree.getSelectionPath().getPath()[1].toString());
        });
        returnBook.addActionListener(e -> {
            int row = tableBorrow.getSelectedRow();
            if(row == -1) return;
            String title = tableBorrow.getValueAt(row, 1).toString();
            logger.info("用户归还了一本图书 "+title);
            mapper.removeBorrow(account.getName(), title);
            gui.showConfirmTip("tip.borrow.return", "tip.button.ok", 200, 150);
            updateList();
            updateBorrowScroll();
            updateScroll(tree.getSelectionPath().getPath()[1].toString());
        });
    }

    /**
     * 当前日期加上天数后的日期
     * @param num 为增加的天数
     * @return 新的日期
     */
     public Date plusDay(int num){
         java.util.Date d;
         Calendar ca = Calendar.getInstance();
         ca.add(Calendar.DATE, num);
         d = ca.getTime();
         return new Date(d.getTime());
     }

    private void selectDefault(){
        if(tree.getSelectionPath() != null && tree.getSelectionPath().getPath().length > 1)
            this.updateScroll(tree.getSelectionPath().getPath()[1].toString());
        this.updateBorrowScroll();
        if(table.getRowCount() > 0){
            table.setRowSelectionInterval(0, 0);
        }
    }

    private void updateBorrowScroll(){
        Object[] head = {"table.book.no", "table.book.title", "table.borrow.start", "table.borrow.end"};
        Object[][] data = new Object[borrows.size()][head.length];
        for (int i = 0; i < borrows.size(); i++) {
            Borrow borrow = borrows.get(i);
            data[i][0] = i;
            data[i][1] = borrow.getTitle();
            data[i][2] = borrow.getStart();
            data[i][3] = borrow.getEnd();
        }
        tableBorrow.setData(data, head);
        tableBorrow.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * 更新分类书籍详细列表
     */
    private void updateScroll(String category){
        List<Book> list = books.get(category)
                .stream()
                .filter(book -> book.getDesc().contains(searchKeyword) || book.getTitle().contains(searchKeyword) ||
                        book.getAuthor().contains(searchKeyword) || (book.getYear() + "").contains(searchKeyword))
                .collect(Collectors.toList());
        Object[] head = {"table.book.no", "table.book.year", "table.book.title", "table.book.author", "table.book.desc"};
        Object[][] data = new Object[list.size()][head.length];
        for (int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            data[i][0] = i;
            data[i][1] = book.getYear();
            data[i][2] = book;
            data[i][3] = book.getAuthor();
            data[i][4] = book.getDesc();
        }
        table.setData(data, head);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setMaxWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setMaxWidth(150);
    }

    /**
     * 更新分类列表
     */
    private void updateList(){
        books.clear();
        borrows.clear();
        all.removeAllChildren();
        BookMapper mapper = DatabaseManager.getBookMapper();
        borrows.addAll(mapper.getUserBorrow(account.getName()));
        mapper.getBookCategory().forEach(category -> {
            books.put(category.getName(), new ArrayList<>());
            DefaultMutableTreeNode type = new DefaultMutableTreeNode(category);
            all.add(type);
            mapper.getBookByCategory(category).forEach(book -> {
                for(Borrow borrow : borrows){
                    if(borrow.getTitle().equals(book.getTitle())) return;
                }
                books.get(category.getName()).add(book);
            });
        });
    }
}
