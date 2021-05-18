package book.manager.dao.mapper;


import book.manager.entity.Book;
import book.manager.entity.Category;
import book.manager.entity.Borrow;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookMapper {
    /**
     * 获取所有的图书类型
     * @return 图书分类
     */
    @Select("select * from DB_BOOK_TYPE")
    List<Category> getBookCategory();

    /**
     * 获取分类所有的的书籍
     * @param category 分类
     * @return 书籍列表
     */
    @Select({"select * from DB_BOOK where type=#{id} order by year desc"})
    List<Book> getBookByCategory(Category category);

    /**
     * 获取所有的借书信息
     * @return 借书信息
     */
    @Select("SELECT `name`, title, author, `desc`, `start`, `end`  FROM DB_LEND LEFT JOIN DB_ACCOUNT ON DB_LEND.account_id = DB_ACCOUNT.id " +
            "LEFT JOIN DB_BOOK ON DB_LEND.book_id = DB_BOOK.id " +
            "WHERE `start` < NOW() && `end` > NOW();")
    List<Borrow> getAllLendInfo();

}
