package org.example.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * OAuth2 登录成功处理器。
 * 当OAuth2认证成功后，此处理器被调用，负责生成JWT并将其通过重定向参数返回给前端。
 */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    // 前端接收JWT的页面地址，您需要根据您的前端项目进行修改
    private final String frontendRedirectUrl = "http://localhost:3000/oauth2/redirect";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        // 从Authentication对象中获取我们自定义的LoginUser
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 为该用户生成JWT
        String token = jwtUtil.generateToken(loginUser);

        // 构建重定向URL，并将JWT作为参数附加
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUrl)
                .queryParam("token", token)
                .build().toUriString();

        // 执行重定向
        response.sendRedirect(redirectUrl);
    }
} 