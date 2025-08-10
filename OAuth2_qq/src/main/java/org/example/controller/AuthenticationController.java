package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.result.ApiResponse;
import org.example.dto.JwtResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.result.ResponseStatus;
import org.example.exc.ApiException;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器，提供用户注册和登录的API接口。
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 处理用户登录请求。
     * @param loginRequest 包含用户名和密码的登录请求体
     * @return 包含JWT的响应体
     */
    @PostMapping("/login")
    public ApiResponse<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtUtil.generateToken(userDetails);

        return ApiResponse.success("登录成功", new JwtResponse(token));
    }

    /**
     * 处理用户注册请求。
     * @param registerRequest 包含用户名和密码的注册请求体
     * @return 注册成功的响应体
     */
    @PostMapping("/register")
    public ApiResponse<Object> register(@RequestBody RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userService.findByUsername(registerRequest.getUsername()) != null) {
            throw new ApiException(ResponseStatus.USERNAME_ALREADY_EXISTS);
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(true);

        userService.save(user);

        return ApiResponse.successMessage("用户注册成功！");
    }
} 