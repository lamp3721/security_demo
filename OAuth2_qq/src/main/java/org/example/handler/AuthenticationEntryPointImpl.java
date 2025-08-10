package org.example.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.result.ApiResponse;
import org.example.result.ResponseStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证入口点实现类，用于处理认证失败（如未登录或Token无效）的情况。
 * 当匿名用户访问需要认证的资源时，会触发此处理器。
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        ResponseStatus status;
        if (authException instanceof BadCredentialsException || authException instanceof UsernameNotFoundException) {
            status = ResponseStatus.INVALID_CREDENTIALS;
        } else if (authException instanceof DisabledException) {
            status = ResponseStatus.ACCOUNT_DISABLED;
        } else {
            status = ResponseStatus.UNAUTHORIZED;
        }

        ApiResponse<Object> apiResponse = ApiResponse.error(status);

        response.setStatus(status.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), apiResponse);
    }
} 