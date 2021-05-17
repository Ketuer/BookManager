package book.mamager.gui;

import dandelion.ui.component.DButton;
import dandelion.ui.component.DPanel;
import dandelion.ui.component.DTab;
import dandelion.ui.gui.Gui;
import dandelion.ui.lang.Text;
import dandelion.ui.tip.Loading;
import dandelion.ui.tip.TipSelect;

public class GuiMain extends Gui {
    public GuiMain(){
        super(new Text("gui.title.main"), 800, 450, true);
    }

    @Override
    protected boolean onLoad(Loading loading) {
        DTab tab = new DTab(this);
        tab.setSize(800, 450);
        DPanel panel1 = new DPanel("首页");
        tab.add(panel1);

        DPanel panel2 = new DPanel("图书管理");
        tab.add(panel2);

        DPanel panel3 = new DPanel("借阅管理");
        tab.add(panel3);

        DButton b = new DButton("退出管理系统", 100, 30, e -> close());

        this.add(b, 695, 5);
        this.add(tab, 0, 0);
        return true;
    }

    @Override
    protected boolean onClose() {
        DButton ok = new DButton("确认");
        DButton cancel = new DButton("取消");
        TipSelect select = new TipSelect(this, 200, 100, "确定要退出图书馆管理系统吗？", ok, cancel);
        return select.displayAndGetResult() == 0;
    }
}
