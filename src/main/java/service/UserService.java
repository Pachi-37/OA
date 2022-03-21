package service;

import dao.RbacDao;
import dao.UserDao;
import entity.Node;
import entity.User;
import service.exception.BusinessException;

import java.util.List;

public class UserService {

    private UserDao userDao = new UserDao();
    private RbacDao rbacDao = new RbacDao();

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

    public List<Node> selectNodeByUserId(Long userId) {
        return rbacDao.selectNodeByUserId(userId);
    }
}
