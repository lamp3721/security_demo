package org.example.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证入口点实现类，用于处理认证失败（如未登录或Token无效）的情况。
 * 当匿名用户访问需要认证的资源时，会触发此处理器。
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        
        // 创建一个标准的失败响应
        ApiResponse<Object> apiResponse = ApiResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "认证失败，请先登录或提供有效的Token。"
        );

        // 设置响应状态码为401 Unauthorized
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 使用ObjectMapper将ApiResponse对象转换为JSON字符串并写入响应体
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), apiResponse);
    }
} 