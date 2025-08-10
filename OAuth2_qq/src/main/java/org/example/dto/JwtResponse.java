package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * JWT响应的数据传输对象 (DTO)。
 * 用于在用户成功登录后，向客户端返回生成的JWT。
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    /**
     * JSON Web Token
     */
    private String token;
} 