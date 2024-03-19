package com.yijiawen.usermanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yijiawen.usermanage.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
* @author 26510
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-03-17 21:41:02
*/
public interface UserService extends IService<User> {


    /**
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    String userRegister(String userAccount,String userPassword,String checkPassword );

    /**
     *
     * @param userAccount
     * @param userPassword
     * @return 返回脱敏用户信息
     */
    User login(String userAccount, String userPassword, HttpServletRequest request);
    User getSafetyUser(User user);

}
