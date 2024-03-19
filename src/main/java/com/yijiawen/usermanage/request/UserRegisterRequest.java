package com.yijiawen.usermanage.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
/***
 * 用户注册请求体
 * */
public class UserRegisterRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = -1526286300432316487L;
   private String userAccount;
    private String     userPassword;
    private String      checkPassword;
}
