package book.manager.dao.mapper;

import book.manager.entity.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    /**
     * 登陆账户
     * @param id 账户id
     * @return 账户
     */
    @Select("select * from DB_ACCOUNT where (id=#{id} or name=#{id}) and password = #{password}")
    Account getAccount(@Param("id") String id,@Param("password") String password);
}
