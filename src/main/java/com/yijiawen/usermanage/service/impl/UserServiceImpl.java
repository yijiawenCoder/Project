package com.yijiawen.usermanage.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yijiawen.usermanage.entity.User;
import com.yijiawen.usermanage.mapper.UserMapper;
import com.yijiawen.usermanage.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yijiawen.usermanage.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 26510
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-03-17 21:41:02
 */


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override

    public String userRegister(String userAccount, String userPassword, String checkPassword) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return null;
        }
        //账户不能重复

        //账户不能包含特殊字符
        String validPatter = "^[a-zA-Z0-9]{6,20}$";
        Matcher matcher = Pattern.compile(validPatter).matcher(userAccount);
        if (!matcher.find()) {
            return null;

        }
        if (!userPassword.equals(checkPassword)) {
            return null;
        }
        //验证账号是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return null;
        }
        //加密

        User user = new User();

        user.setUserName(user.getUserId());
        user.setUserAccount(userAccount);
        user.setAvatarURL("null");
        user.setGender(0);
        user.setUserPassword(passwordEncoder.encode(userPassword));
        user.setPhoneNumber("null");
        user.setEmail("");
        user.setUserState(0);
        user.setCreateTime(new Timestamp(new java.util.Date().getTime()));
        user.setUpdateTime(new Timestamp(new java.util.Date().getTime()));
        user.setIsDelete(0);
        user.setUserRole(0);

        this.save(user);

        return user.getUserId();
    }

    @Override
    public User login(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }


        //账户不能包含特殊字符
        String validPatter = "^[a-zA-Z0-9]{6,20}$";
        Matcher matcher = Pattern.compile(validPatter).matcher(userAccount);
        if (!matcher.find()) {
            return null;

        }
        //对用户输入的密码加密
        String encode = passwordEncoder.encode(userPassword);
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
       // queryWrapper.eq("user_password", );
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            log.info("user login failed,userAccount cannt  match  his pwd");
            return null;
        }
        if(!passwordEncoder.matches(user.getUserPassword(),encode))
        {
            return null;
        }

        //脱敏
        User safetyUser = getSafetyUser(user);

        //设置用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

      //返回脱敏后的用户信息
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User user) {
        User newUser = new User();
        newUser.setUserId(user.getUserId());
        newUser.setUserName(user.getUserName());
        newUser.setUserAccount(user.getUserAccount());
        newUser.setAvatarURL(user.getAvatarURL());
        newUser.setGender(user.getGender());

        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setEmail(user.getEmail());
        newUser.setUserState(user.getUserState());
        newUser.setCreateTime(user.getCreateTime());
        newUser.setUpdateTime(user.getUpdateTime());
        return newUser;
    }
}




