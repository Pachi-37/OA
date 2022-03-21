package dao;

import entity.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDaoTest {

    @Test
    public void selectByUserName() {
        UserDao userDao = new UserDao();
        User user = userDao.selectByUserName("t8");
    }
}