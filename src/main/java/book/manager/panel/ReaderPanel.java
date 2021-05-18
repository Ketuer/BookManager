package book.manager.panel;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.BookMapper;
import book.manager.entity.Book;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.*;
import dandelion.ui.lang.Text;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReaderPanel extends DPanel {

    private final Map<String, List<Book>> books = new HashMap<>();
    DefaultMutableTreeNode all = new DefaultMutableTreeNode("tree.top.book");
    DTree tree = new DTree(all, 170, 370);

    DButton refresh = new DButton("button.func.refresh", 170, 30, e -> this.updateList());

    DTable table = new DTable();
    DScroll detailScroll = new DScroll(560, 390, table);
    DIcon searchIcon = new DIcon("/light/input_search.png", DIcon.JAR);
    DTextField search = new DTextField(searchIcon, 470, 25, "hint.search");
    DButton borrowBook = new DButton("button.book.borrow", 80, 25);

    public ReaderPanel() {
        super("panel.title.reader");
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
        this.add(detailScroll, 205, 40);
        searchIcon.registerColorConfig(ColorSwitch.DARK, "/dark/input_search.png", DIcon.JAR);
        this.add(search, 205, 10);
        this.add(borrowBook, 685, 10);
    }

    private void selectDefault(){
        if(tree.getSelectionPath() != null && tree.getSelectionPath().getPath().length > 1)
            this.updateScroll(tree.getSelectionPath().getPath()[1].toString());
        if(table.getRowCount() > 0){
            table.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * 更新分类书籍详细列表
     */
    private void updateScroll(String category){
        List<Book> list = books.get(category);
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
        all.removeAllChildren();
        BookMapper mapper = DatabaseManager.getBookMapper();
        mapper.getBookCategory().forEach(category -> {
            books.put(category.getName(), new ArrayList<>());
            DefaultMutableTreeNode type = new DefaultMutableTreeNode(category);
            all.add(type);
            mapper.getBookByCategory(category).forEach(book -> books.get(category.getName()).add(book));
        });
    }
}
