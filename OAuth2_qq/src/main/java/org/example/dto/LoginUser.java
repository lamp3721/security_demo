package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security所需的用户详情对象。
 * 用于将我们自己的User实体和权限信息适配成Spring Security能够识别的格式。
 */
public class LoginUser implements UserDetails {

    /**
     * 我们的用户实体对象。
     */
    private final User user;

    /**
     * 用户的权限编码列表 (e.g., "sys:user:list", "sys:role:add")。
     */
    private final List<String> permissions;

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public User getUser() {
        return user;
    }

    /**
     * 获取用户的权限集合。
     * Spring Security会使用这个方法来确定用户的授权信息。
     * @return 权限集合
     */
    @Override
    @JsonIgnore // 在序列化为JSON时忽略此字段
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将权限字符串列表转换为Spring Security的GrantedAuthority对象列表
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

    /**
     * 账户是否未过期。
     * @return true 表示未过期
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true; // 可以根据业务需求添加账户过期的逻辑
    }

    /**
     * 账户是否未被锁定。
     * @return true 表示未锁定
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true; // 可以根据业务需求添加账户锁定的逻辑
    }

    /**
     * 凭证（密码）是否未过期。
     * @return true 表示未过期
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true; // 可以根据业务需求添加凭证过期的逻辑
    }

    /**
     * 账户是否已启用。
     * @return true 表示已启用
     */
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
} 