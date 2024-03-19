package com.yijiawen.usermanage.service;
import java.sql.Timestamp;
import java.util.Date;


import com.yijiawen.usermanage.entity.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * 用户服务测试
 * @author yijiawen
 *
 * */

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();

        user.setUserName(null);
        user.setUserAccount("123");
        user.setAvatarURL("u");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhoneNumber("333");
        user.setEmail("333@123");
        user.setUserState(0);
        user.setCreateTime(  new Timestamp(new java.util.Date().getTime()));
        user.setUpdateTime( new Timestamp(new java.util.Date().getTime()));
        user.setIsDelete(0);


        boolean rs = userService.save(user);

        System.out.println(user.getUserId());
        Assertions.assertTrue(rs);
    }

    @Test
    void userRegister() {

        String account = "ERGVWREBGEWRTQER";
        String password = "111fqwefew";
        String checkPassword = "111fqwefew";
        String s = userService.userRegister(account, password, checkPassword);
        System.out.println(s);


    }
}