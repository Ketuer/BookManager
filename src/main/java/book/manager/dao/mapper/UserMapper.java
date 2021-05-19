package book.manager.dao.mapper;

import book.manager.entity.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    /**
     * 登陆账户
     * @param id 账户id
     * @return 账户
     */
    @Select("select * from DB_ACCOUNT where (id=#{id} or name=#{id}) and password = #{password}")
    Account getAccount(@Param("id") String id,@Param("password") String password);

    /**
     * 获取所有的读者
     * @return 读者账户列表
     */
    @Select("select * from DB_ACCOUNT where role = 'Reader'")
    List<Account> getReaders();

    @Insert("insert into DB_ACCOUNT (name, sex, role, password, header, note) " +
            "values(#{name}, #{sex}, #{role}, #{password}, #{header}, #{note})")
    void createUser(Account account);

    @Delete("delete from DB_ACCOUNT where name = #{name}")
    void removeUser(String name);

    @Update("update DB_ACCOUNT set name = #{account.name}, note = #{account.note}, sex = #{account.sex} where name = #{oldName}")
    void updateUser(@Param("account") Account account, @Param("oldName") String oldName);
}
