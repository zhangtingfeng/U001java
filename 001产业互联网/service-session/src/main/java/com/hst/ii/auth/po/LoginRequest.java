package com.hst.ii.auth.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录请求
 */
@Data
@NoArgsConstructor
public class LoginRequest {
    private String userid;
    private String passwd;
    private boolean autoLogin;
}
