package book.manager.dao.mapper;


import book.manager.entity.Book;
import book.manager.entity.Category;
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

}
