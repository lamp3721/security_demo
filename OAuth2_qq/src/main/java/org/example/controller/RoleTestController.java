package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于测试基于【角色】（Role）进行访问控制的控制器。
 */
@RestController
@RequestMapping("/api/test/role")
@RequiredArgsConstructor
public class RoleTestController {

    /**
     * 这是一个需要 'ADMIN' 角色才能访问的接口。
     * 注意：'ADMIN' 会被自动加上 'ROLE_' 前缀，实际检查的是 'ROLE_ADMIN'。
     * @return 成功信息
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> adminRoleTest() {
        return ApiResponse.success("成功访问：此接口仅限【管理员】角色。");
    }

    /**
     * 这是一个需要 'USER' 或 'ADMIN' 角色才能访问的接口。
     * @return 成功信息
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ApiResponse<String> userRoleTest() {
        return ApiResponse.success("成功访问：【管理员】或【普通用户】角色均可访问此接口。");
    }

    /**
     * 这是一个需要登录后才能访问的通用接口，不限制特定角色或权限。
     * @return 问候语
     */
    @GetMapping("/hello")
    public ApiResponse<String> hello() {
        return ApiResponse.success("你好，已登录的用户！这里是角色测试控制器。");
    }
} 