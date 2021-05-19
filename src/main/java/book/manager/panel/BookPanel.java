package book.manager.panel;

import book.manager.dao.DatabaseManager;
import book.manager.dao.mapper.BookMapper;
import book.manager.entity.Book;
import book.manager.tip.TipAddBook;
import book.manager.tip.TipAddCategory;
import dandelion.ui.color.ColorSwitch;
import dandelion.ui.component.*;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.TipConfirm;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookPanel extends DPanel {

    private final Map<String, List<Book>> books = new HashMap<>();
    DTree tree;

    DButton refresh = new DButton("button.func.refresh", 100, 30);
    DButton add = new DButton("+", 30, 30);
    DButton decrease = new DButton("-", 30, 30);

    DTable table = new DTable();
    DScroll detailScroll = new DScroll(560, 200, table);
    DIcon searchIcon = new DIcon("/light/input_search.png", DIcon.JAR);
    DTextField search = new DTextField(searchIcon, 390, 25, "hint.search");
    DButton addBook = new DButton("button.book.add", 80, 25);
    DButton remBook = new DButton("button.book.remove", 80, 25);

    DLabel titleEditor = new DLabel("label.editor");
    DLabel bookNameLabel = new DLabel("label.title");
    DTextField bookName = new DTextField(null, 150, 25, "hint.input.title");
    DLabel bookAuthorLabel = new DLabel("label.author");
    DTextField bookAuthor = new DTextField(null, 150, 25, "hint.input.author");
    DLabel bookTimeLabel = new DLabel("label.time");
    DSelect<Text> bookTime = new DSelect<>(150, 25,
            new Text("2021"), new Text("2020"), new Text("2019"), new Text("2018"));
    DLabel bookDescLabel = new DLabel("label.desc");
    DTextArea bookDesc = new DTextArea(310, 120, "hint.input.desc");
    DButton saveEdit = new DButton("button.book.save", 100, 30);

    String searchKeyWord = "";

    Gui gui;
    public BookPanel(Gui gui) {
        super("panel.title.book");

        this.gui = gui;

        this.initCategoryTree();
        this.initBookTable();
        this.initEditPanel();
    }

    /**
     * 初始化编辑面板
     */
    private void initEditPanel(){
        BookMapper mapper = DatabaseManager.getBookMapper();
        DPanel editPanel = new DPanel("内容编辑", 560, 170);
        this.add(editPanel, 205, 20);
        titleEditor.setFont(new Font("", Font.PLAIN, 15));
        DIcon icon = new DIcon("/light/image_editor.png", DIcon.JAR);
        DImage image = new DImage(20, 20, icon.getImage());
        editPanel.add(image, 20, 14);
        editPanel.add(titleEditor, 50, 14);
        bookNameLabel.setFont(new Font("", Font.PLAIN, 15));
        editPanel.add(bookNameLabel, 10, 44);
        editPanel.add(bookName, 70, 40);
        bookAuthorLabel.setFont(new Font("", Font.PLAIN, 15));
        editPanel.add(bookAuthorLabel, 10, 74);
        editPanel.add(bookAuthor, 70, 70);
        bookTimeLabel.setFont(new Font("", Font.PLAIN, 15));
        editPanel.add(bookTimeLabel, 10, 104);
        editPanel.add(bookTime, 70, 100);
        bookDescLabel.setFont(new Font("", Font.PLAIN, 15));
        editPanel.add(bookDescLabel, 240, 14);
        editPanel.add(bookDesc, 240, 40);

        DButton.ButtonColorConfig config = saveEdit.getColorConfig(ColorSwitch.LIGHT);
        config.backgroundColor = new Color(81, 168, 238);
        config.fontColor = Color.white;
        saveEdit.getColorConfig(ColorSwitch.DARK).backgroundColor = new Color(61, 141, 205);
        DIcon iconSave = new DIcon("/dark/button_save.png", DIcon.JAR);
        iconSave.registerColorConfig(ColorSwitch.DARK, "/dark/button_save.png", DIcon.JAR);
        saveEdit.setIcon(iconSave);
        saveEdit.addActionListener(e -> {
            String title = this.bookName.getText();
            String author = this.bookAuthor.getText();
            String year = this.bookTime.getSelectedItem().toString();
            String desc = this.bookDesc.getText();

            if(title.isEmpty() || author.isEmpty() || desc.isEmpty()){
                TipConfirm confirm = new TipConfirm(gui, "tip.edit.finish", "tip.button.ok", 200, 150);
                confirm.setAlwaysOnTop(true);
                confirm.display();
            }else{
                Book book = new Book(title, author, desc, Integer.parseInt(year));
                mapper.updateBook(book, tree.getSelectionPath().getPath()[1].toString());
                gui.showConfirmTip("tip.edit.save", "tip.button.ok", 200, 150);
                updateList();
                selectDefault();
            }
        });
        editPanel.add(saveEdit, 80, 130);
    }

    /**
     * 初始化图书列表
     */
    private void initBookTable(){
        BookMapper mapper = DatabaseManager.getBookMapper();
        table.setFont(new Font("", Font.PLAIN, 14));
        table.setRowHeight(20);
        this.add(detailScroll, 205, 230);
        searchIcon.registerColorConfig(ColorSwitch.DARK, "/dark/input_search.png", DIcon.JAR);
        this.add(search, 205, 200);
        this.add(addBook, 600, 200);
        this.add(remBook, 685, 200);
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                searchKeyWord = search.getText();
                updateScroll(tree.getSelectionPath().getPath()[1].toString());
                selectDefaultLine();
            }
        });
        remBook.addActionListener(e -> {
            if(bookName.getText().isEmpty()) return;
            DButton ok = new DButton("tip.button.ok");
            DButton cancel = new DButton("tip.button.cancel");
            if(gui.showSelectTip("tip.book.delete", 200, 100, ok, cancel) == 0){
                mapper.removeBook(bookName.getText());
                gui.showConfirmTip("tip.book.delete.ok", "tip.button.ok", 200, 150);
                updateList();
                selectDefault();
                tree.expandRow(0);
                tree.setSelectionRow(1);
            }
        });
        addBook.addActionListener(e -> {
            boolean success = new TipAddBook(gui, books.keySet()).getResult();
            if(success){
                gui.showConfirmTip("tip.book.add.ok", "tip.button.ok", 200, 150);
                updateList();
                selectDefault();
                tree.expandRow(0);
                tree.setSelectionRow(1);
            }
        });
    }

    /**
     * 初始化分类列表
     */
    private void initCategoryTree(){
        BookMapper mapper = DatabaseManager.getBookMapper();
        this.updateList();
        tree.expandRow(0);
        tree.setSelectionRow(1);
        this.add(refresh, 90, 400);
        this.add(add, 20, 400);
        this.add(decrease, 55, 400);
        refresh.addActionListener( e -> {
            this.updateList();
            this.selectDefault();
            tree.expandRow(0);
            tree.setSelectionRow(1);
        });
        add.addActionListener(e -> {
            TipAddCategory addCategory = new TipAddCategory(gui);
            boolean res = addCategory.getResult();
            if(res){
                gui.showConfirmTip("tip.category.add.ok", "tip.button.ok", 200, 150);
                updateList();
                tree.expandRow(0);
                tree.setSelectionRow(1);
                selectDefault();
            }
        });
        decrease.addActionListener(e -> {
            DButton ok = new DButton("tip.button.ok");
            DButton cancel = new DButton("tip.button.cancel");
            if(gui.showSelectTip("tip.category.delete", 200, 100, ok, cancel) == 0){
                String category = tree.getSelectionPath().getPath()[1].toString();
                mapper.removeBookByCategory(category);
                mapper.removeCategory(category);
                gui.showConfirmTip("tip.category.delete.ok", "tip.button.ok", 200, 150);
                updateList();
                tree.expandRow(0);
                tree.setSelectionRow(1);
                selectDefault();
            }
        });
        this.selectDefault();
    }

    /**
     * 选择默认的分类、书籍信息
     */
    private void selectDefault(){
        if(tree.getSelectionPath() != null && tree.getSelectionPath().getPath().length > 1)
            this.updateScroll(tree.getSelectionPath().getPath()[1].toString());
        this.selectDefaultLine();
    }

    /**
     * 选择默认的书籍信息
     */
    private void selectDefaultLine(){
        if(table.getRowCount() > 0){
            table.setRowSelectionInterval(0, 0);
            Object o = table.getModel().getValueAt(0, 2);
            if(o != null)
                this.updateEditor((Book) o);
        }
    }

    /**
     * 更新边界框内的内容
     * @param book 书籍
     */
    private void updateEditor(Book book){
        bookName.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookTime.setSelectedIndex(2021 - book.getYear());
        bookDesc.setText(book.getDesc());
    }

    /**
     * 更新分类书籍详细列表
     */
    private void updateScroll(String category){
        List<Book> list = books.get(category)
                .stream()
                .filter(book -> book.getTitle().contains(searchKeyWord) || book.getAuthor().contains(searchKeyWord) ||
                        book.getDesc().contains(searchKeyWord) || (book.getYear()+"").contains(searchKeyWord))
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
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0){
                    Book book = (Book) table.getModel().getValueAt(row, 2);
                    updateEditor(book);
                }
            }
        });
    }

    /**
     * 更新分类列表
     */
    private void updateList(){
        books.clear();
        if(tree != null) this.remove(tree);
        DefaultMutableTreeNode all = new DefaultMutableTreeNode("tree.top.book");
        tree = new DTree(all, 170, 370);
        BookMapper mapper = DatabaseManager.getBookMapper();
        mapper.getBookCategory().forEach(category -> {
            books.put(category.getName(), new ArrayList<>());
            DefaultMutableTreeNode type = new DefaultMutableTreeNode(category);
            all.add(type);
            mapper.getBookByCategory(category).forEach(book -> books.get(category.getName()).add(book));
        });
        this.add(tree, 20, 20);
        tree.setFont(new Font("", Font.PLAIN, 14));
        tree.addTreeSelectionListener(e -> this.selectDefault());
    }
}
