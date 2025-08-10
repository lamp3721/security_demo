package org.example.service.impl;

import org.example.domain.LoginUser;
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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Find user by username from the database
        User user = userMapper.selectByUsername(username);

        // 2. If user does not exist, throw an exception
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 3. Find the user's permissions
        List<String> permissions = permissionMapper.selectPermissionsByUserId(user.getId());

        // 4. Encapsulate the user and permissions into a LoginUser object
        return new LoginUser(user, permissions);
    }
} 