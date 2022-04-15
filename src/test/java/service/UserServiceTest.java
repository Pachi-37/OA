package service;

import entity.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {

    UserService userService = new UserService();

    @Test
    public void checkLogin() {
        User user = userService.checkLogin("m8", "test");
        System.out.println(user);
    }

    @Test
    public void selectNodeByUserId() {
        userService.selectNodeByUserId(21L).stream().forEach(s -> System.out.println(s));
    }

    @Test
    public void onboarding() {
        User user = new User();
        user.setUserName("test");
        user.setSalt(1);
        user.setPassword("weq");
        user.setEmployeeId(1l);

        userService.onboarding(user);
    }
}