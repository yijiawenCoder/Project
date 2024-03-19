package com.yijiawen.usermanage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yijiawen.usermanage.entity.User;
import com.yijiawen.usermanage.request.UserLoginRequest;
import com.yijiawen.usermanage.request.UserRegisterRequest;
import com.yijiawen.usermanage.service.UserService;
import com.yijiawen.usermanage.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yijiawen.usermanage.constant.UserConstant.Admin_Role;
import static com.yijiawen.usermanage.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public String userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();

        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getAccount();
        String userPassword = userLoginRequest.getPassword();


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.login(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        // 用户鉴权
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }


        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody String userId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return false;
        }
        if (userId == null) {
            return false;
        }

        return userService.removeById(userId);
    }

    /**
     * 是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        if (user == null || user.getUserRole() != Admin_Role) {
            return false;
        }
        return true;
    }
}
