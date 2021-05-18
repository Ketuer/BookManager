package book.manager.panel;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.BookMapper;
import book.manager.entity.Book;
import book.manager.entity.Category;
import dandelion.ui.component.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookPanel extends DPanel {

    private final Map<String, List<Book>> books = new HashMap<>();
    DefaultMutableTreeNode all = new DefaultMutableTreeNode("tree.top.book");
    DTree tree = new DTree(all, 170, 320);

    DButton refresh = new DButton("button.func.refresh", 100, 30, e -> this.updateList());
    DButton add = new DButton("+", 30, 30);
    DButton decrease = new DButton("-", 30, 30);

    DTable table = new DTable();
    DScroll detailScroll = new DScroll(460, 200, table);

    public BookPanel() {
        super("panel.title.book");

        this.updateList();
        this.add(tree, 20, 20);
        tree.expandRow(0);
        tree.setSelectionRow(1);
        tree.addTreeSelectionListener(this::updateScroll);

        this.add(refresh, 90, 350);
        this.add(add, 20, 350);
        this.add(decrease, 55, 350);

        this.add(detailScroll, 205, 180);
    }

    /**
     * 更新分类书籍详细列表
     */
    private void updateScroll(TreeSelectionEvent e){
        String category = e.getPath().getPath()[1].toString();
        System.out.println(category);
        List<Book> list = books.get(category);
        Object[] head = {"序号", "书籍名称", "作者", "年份", "简介"};
        Object[][] data = new Object[list.size()][head.length];
        for (int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            data[i][0] = i;
            data[i][1] = book.getTitle();
            data[i][2] = book.getAuthor();
            data[i][3] = book.getYear();
            data[i][4] = book.getDesc();
        }
        table.setData(data, head);
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
