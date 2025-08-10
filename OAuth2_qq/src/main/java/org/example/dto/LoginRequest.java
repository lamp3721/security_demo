package org.example.dto;

import lombok.Data;

/**
 * 用户登录请求的数据传输对象 (DTO)。
 * 用于封装客户端在登录时提交的用户名和密码。
 */
@Data
public class LoginRequest {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
} 