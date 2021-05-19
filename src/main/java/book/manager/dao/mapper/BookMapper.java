package book.manager.dao.mapper;


import book.manager.entity.Book;
import book.manager.entity.Borrow;
import book.manager.entity.Category;
import org.apache.ibatis.annotations.*;

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
    @Select({"select title, author, description, year_time from DB_BOOK where type=#{id} order by year_time desc"})
    List<Book> getBookByCategory(Category category);

    /**
     * 获取所有的借书信息
     * @return 借书信息
     */
    @Select("SELECT `name`, title, author, description, `start`, `end`  FROM DB_LEND LEFT JOIN DB_ACCOUNT ON DB_LEND.account_id = DB_ACCOUNT.id " +
            "LEFT JOIN DB_BOOK ON DB_LEND.book_id = DB_BOOK.id " +
            "WHERE `start` < NOW() && `end` > NOW();")
    List<Borrow> getAllLendInfo();

    @Delete("delete from DB_BOOK where title = #{name}")
    void removeBook(String name);

    @Insert("insert into DB_BOOK (title, author, description, year_time, type) " +
            "values(#{book.title}, #{book.author}, #{book.desc}, #{book.year}, (select id from DB_BOOK_TYPE where `name` = #{category}))")
    void insertBook(@Param("book") Book book, @Param("category") String category);

    @Update("update DB_BOOK set title = #{book.title}, author = #{book.author}, description = #{book.desc}, year_time = #{book.year}, type = (select id from DB_BOOK_TYPE where `name` = #{category})" +
            " where title = #{book.title}")
    void updateBook(@Param("book") Book book, @Param("category") String category);

    @Insert("insert into DB_BOOK_TYPE (id, name) values(#{id}, #{name})")
    void addCategory(Category category);

    @Delete("delete from DB_BOOK_TYPE where name = #{category}")
    void removeCategory(String category);

    @Delete("delete from DB_BOOK where type = (select id from DB_BOOK_TYPE where name = #{category})")
    void removeBookByCategory(String category);

    @Delete("delete from DB_LEND where account_id = (select id from DB_ACCOUNT where name = #{aid}) " +
            "and book_id = (select id from DB_BOOK where title = #{bid})")
    void removeBorrow(@Param("aid") String aid, @Param("bid") String bid);
}
