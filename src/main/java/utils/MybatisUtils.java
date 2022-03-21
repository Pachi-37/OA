package utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory = null;

    static {
        Reader reader = null;

        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Object executeQuery(Function<SqlSession, Object> function) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object object = function.apply(sqlSession);
            return object;
        } finally {
            sqlSession.close();
        }
    }

    public static Object executeUpdate(Function<SqlSession, Object> function) {
        SqlSession sqlSession = sqlSessionFactory.openSession(false);

        try {
            Object object = function.apply(sqlSession);
            sqlSession.commit();
            return object;
        } catch (RuntimeException e) {
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    private static SqlSession openSession() {
        return sqlSessionFactory.openSession();
    }

    private static void closeSession(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }
}
