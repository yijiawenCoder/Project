package com.yijiawen.usermanage.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1074674832023366131L;
    private String account;
    private  String password;
}
