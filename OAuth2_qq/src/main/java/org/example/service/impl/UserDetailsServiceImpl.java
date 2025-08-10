package org.example.service.impl;

import org.example.dto.LoginUser;
import org.example.domain.User;
import org.example.mapper.PermissionMapper;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 实现了Spring Security的UserDetailsService接口。
 * 用于在认证过程中根据用户名从数据库加载用户信息和权限。
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 根据用户名加载用户详细信息。
     * @param username 用户名
     * @return UserDetails对象，包含了用户信息和权限
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据用户名从数据库查询用户信息
        User user = userMapper.selectByUsername(username);

        // 2. 如果用户不存在，抛出异常
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 3. 查询用户的权限信息
        List<String> permissions = permissionMapper.selectPermissionsByUserId(user.getId());

        // 4. 将用户和权限信息封装成LoginUser对象
        return new LoginUser(user, permissions);
    }
} 