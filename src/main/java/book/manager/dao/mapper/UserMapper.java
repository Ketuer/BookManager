package book.manager.dao.mapper;

import book.manager.entity.Account;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    /**
     * 登陆账户
     * @param id 账户id
     * @return 账户
     */
    @Select("select * from DB_ACCOUNT where id=#{id}")
    Account getAccount(String id);
}
