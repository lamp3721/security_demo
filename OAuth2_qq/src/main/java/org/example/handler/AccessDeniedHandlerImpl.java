package org.example.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权失败处理器实现类。
 * 当已认证的用户尝试访问其没有权限的资源时，会触发此处理器。
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        
        // 创建一个标准的失败响应
        ApiResponse<Object> apiResponse = ApiResponse.error(
                HttpStatus.FORBIDDEN.value(),
                "权限不足，无法访问此资源。"
        );

        // 设置响应状态码为403 Forbidden
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 使用ObjectMapper将ApiResponse对象转换为JSON字符串并写入响应体
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), apiResponse);
    }
} 