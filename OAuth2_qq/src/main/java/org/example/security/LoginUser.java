package org.example.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.example.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring Security所需的用户详情对象, 实现了UserDetails和OAuth2User接口。
 * 它是连接我们的用户实体和Spring Security框架的统一适配器。
 */
public class LoginUser implements UserDetails, OAuth2User {

    @Getter
    private final User user;
    private final List<String> permissions;

    // 用于存储从OAuth2 Provider获取的用户属性
    @Getter
    private Map<String, Object> attributes;

    // 用于标准登录
    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
    
    // 用于OAuth2登录
    public LoginUser(User user, List<String> permissions, Map<String, Object> attributes) {
        this.user = user;
        this.permissions = permissions;
        this.attributes = attributes;
    }
    
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptyList();
        }
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    /**
     * 实现OAuth2User接口的方法，返回用户名作为OAuth2用户的name。
     * @return 用户名
     */
    @Override
    public String getName() {
        return user.getUsername();
    }
} 