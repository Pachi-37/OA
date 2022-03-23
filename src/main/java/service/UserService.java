package service;

import dao.RbacDao;
import dao.UserDao;
import entity.Node;
import entity.User;
import service.exception.BusinessException;
import utils.MD5Utils;

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

        String md5 = MD5Utils.md5Digest(password, user.getSalt());
        if (md5.equals(user.getPassword())) {
            return user;
        } else {
            throw new BusinessException("L002", "密码错误");
        }
    }

    public List<Node> selectNodeByUserId(Long userId) {
        return rbacDao.selectNodeByUserId(userId);
    }
}
