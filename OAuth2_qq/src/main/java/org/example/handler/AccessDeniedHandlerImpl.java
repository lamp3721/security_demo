package org.example.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.result.ApiResponse;
import org.example.result.ResponseStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权失败处理器实现类。
 * 当已认证的用户尝试访问其没有权限的资源时，会触发此处理器。
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        
        ResponseStatus status = ResponseStatus.FORBIDDEN;
        ApiResponse<Object> apiResponse = ApiResponse.error(status);

        response.setStatus(status.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), apiResponse);
    }
} 