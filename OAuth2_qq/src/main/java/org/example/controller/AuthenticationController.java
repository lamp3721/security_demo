package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.JwtResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.domain.User;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
     * @return 包含JWT的响应实体
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 使用AuthenticationManager进行用户认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // 将认证信息存入SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 从认证信息中获取UserDetails
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 生成JWT
        final String token = jwtUtil.generateToken(userDetails);

        // 返回包含JWT的响应
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * 处理用户注册请求。
     * @param registerRequest 包含用户名和密码的注册请求体
     * @return 注册成功或失败的响应实体
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userService.findByUsername(registerRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("错误：该用户名已被占用！");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        // 对密码进行加密处理
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // 默认设置为启用状态
        user.setEnabled(true);

        userService.save(user);

        return ResponseEntity.ok("用户注册成功！");
    }
} 