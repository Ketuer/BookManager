package book.manager.dao;

import book.manager.dao.mapper.BookMapper;
import book.manager.dao.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.Reader;

public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class);
    private final SqlSession sqlSession;

    private static DatabaseManager instance;

    private DatabaseManager(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }

    public static boolean init(){
        try {
            logger.info("正在初始化数据库...");
            Reader reader = Resources.getResourceAsReader("MapperConfig.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            SqlSession sqlSession = sqlSessionFactory.openSession();
            instance = new DatabaseManager(sqlSession);
            logger.info("数据库初始化完成...");
            return true;
        } catch (Exception e) {
            logger.error("初始化数据库时出现错误！", e);
            return false;
        }
    }

    public static BookMapper getBookMapper(){
        if(instance == null) init();
        return instance.sqlSession.getMapper(BookMapper.class);
    }

    public static UserMapper getUserMapper(){
        if(instance == null) init();
        return instance.sqlSession.getMapper(UserMapper.class);
    }

    public static void close(){
        instance.sqlSession.close();
        instance = null;
    }
}
