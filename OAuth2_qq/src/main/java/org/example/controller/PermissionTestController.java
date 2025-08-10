package org.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于测试基于【权限】（Authority）进行访问控制的控制器。
 */
@RestController
@RequestMapping("/api/test/permission")
public class PermissionTestController {

    /**
     * 这是一个需要 'sys:user:list' 权限才能访问的接口。
     * @return 成功信息
     */
    @GetMapping("/user/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public String listUsers() {
        return "成功访问：用户列表资源（需要 'sys:user:list' 权限）。";
    }

    /**
     * 这是一个需要 'sys:role:add' 权限才能访问的接口。
     * @return 成功信息
     */
    @GetMapping("/role/add")
    @PreAuthorize("hasAuthority('sys:role:add')")
    public String addRole() {
        return "成功访问：添加角色资源（需要 'sys:role:add' 权限）。";
    }

    /**
     * 这是一个需要 'sys:admin:manage' 权限才能访问的接口。
     * @return 成功信息
     */
    @GetMapping("/admin/manage")
    @PreAuthorize("hasAuthority('sys:admin:manage')")
    public String manageAdmin() {
        return "成功访问：系统管理资源（需要 'sys:admin:manage' 权限）。";
    }
} 