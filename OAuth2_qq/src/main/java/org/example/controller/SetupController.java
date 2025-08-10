package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.domain.*;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 用于一键初始化测试数据的控制器。
 * 警告：此控制器仅用于开发和测试环境，请勿在生产环境中暴露。
 */
@RestController
@RequestMapping("/api/setup")
public class SetupController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 初始化测试数据。
     * 此操作是幂等的，重复执行不会创建重复数据。
     * @return 成功信息
     */
    @PostMapping("/init")
    @Transactional
    public String initData() {
        // 1. 创建权限
        Permission p1 = getOrCreatePermission("查看用户", "sys:user:list", 2);
        Permission p2 = getOrCreatePermission("添加角色", "sys:role:add", 3);
        Permission p3 = getOrCreatePermission("系统管理", "sys:admin:manage", 1);

        // 2. 创建角色
        Role adminRole = getOrCreateRole("管理员", "ROLE_ADMIN");
        Role userRole = getOrCreateRole("普通用户", "ROLE_USER");

        // 3. 关联角色与权限
        // 管理员拥有所有权限
        assignPermissionToRole(adminRole, p1, p2, p3);
        // 普通用户只拥有查看用户权限
        assignPermissionToRole(userRole, p1);

        // 4. 创建用户
        User adminUser = getOrCreateUser("admin", "123456");
        User normalUser = getOrCreateUser("user", "123456");

        // 5. 关联用户与角色
        assignRoleToUser(adminUser, adminRole);
        assignRoleToUser(normalUser, userRole);

        return "测试数据初始化成功！创建了 'admin' 和 'user' 两个用户，密码均为 '123456'。";
    }

    private Permission getOrCreatePermission(String name, String code, int type) {
        Permission permission = permissionService.getOne(new LambdaQueryWrapper<Permission>().eq(Permission::getCode, code));
        if (permission == null) {
            permission = new Permission();
            permission.setName(name);
            permission.setCode(code);
            permission.setType(PermissionType.values()[type-1]);
            permissionService.save(permission);
        }
        return permission;
    }

    private Role getOrCreateRole(String name, String code) {
        Role role = roleService.getOne(new LambdaQueryWrapper<Role>().eq(Role::getCode, code));
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setCode(code);
            roleService.save(role);
        }
        return role;
    }

    private User getOrCreateUser(String username, String password) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
            userService.save(user);
        }
        return user;
    }

    private void assignPermissionToRole(Role role, Permission... permissions) {
        List<Long> existingPermissionIds = rolePermissionService.list(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, role.getId())
        ).stream().map(RolePermission::getPermissionId).toList();

        Arrays.stream(permissions).forEach(p -> {
            if (!existingPermissionIds.contains(p.getId())) {
                RolePermission rp = new RolePermission();
                rp.setRoleId(role.getId());
                rp.setPermissionId(p.getId());
                rolePermissionService.save(rp);
            }
        });
    }
    
    private void assignRoleToUser(User user, Role role) {
        long count = userRoleService.count(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, user.getId())
                .eq(UserRole::getRoleId, role.getId()));
        if (count == 0) {
            UserRole ur = new UserRole();
            ur.setUserId(user.getId());
            ur.setRoleId(role.getId());
            userRoleService.save(ur);
        }
    }
} 