package service;

import dao.UserDao;
import entity.User;
import service.exception.BusinessException;

public class UserService {

    private UserDao userDao = new UserDao();

    public User checkLogin(String username, String password) {

        User user = userDao.selectByUserName(username);

        if (user == null) {
            // 用户不存在，抛出异常
            throw new BusinessException("L001", "用户名不存在");
        }

        if (password.equals(user.getPassword())) {
            return user;
        } else {
            throw new BusinessException("L002", "密码错误");
        }
    }
}
