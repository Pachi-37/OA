package service;

import entity.User;
import junit.framework.TestCase;

public class UserServiceTest extends TestCase {

    private UserService userService = new UserService();

    public void testCheckLogin() {
        User user = userService.checkLogin("m8", "test");

        System.out.println(user.getPassword());
    }
}