package dao;

import entity.User;
import utils.MybatisUtils;

/**
 * 用户表 UserDao
 */
public class UserDao {

    /**
     * 按用户名查询用户表
     *
     * @param username 用户名
     * @return User 对象包含对应的用户信息，null 表示用户不存在
     */
    public User selectByUserName(String username) {
        User user = (User) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("usermapper.selectByUsername", username));
        return user;
    }
}
