package org.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.domain.UserRole;
import org.example.service.RoleService;
import org.example.service.UserRoleService;
import org.example.service.UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("qq".equals(registrationId)) {
            // 1. 获取 OpenID
            String openId = getOpenId(userRequest);
            log.info("成功获取QQ用户的OpenID: {}", openId);

            // 2. 根据OpenID查找或创建用户
            User user = userService.findByUsername("qq_" + openId);
            if (user == null) {
                log.info("系统中不存在该QQ用户，将为其自动注册。");
                user = createNewQqUser(openId);
            } else {
                log.info("该QQ用户已存在于系统中，直接登录。");
            }

            // 3. 将用户信息封装成LoginUser，它同时实现了UserDetails和OAuth2User
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("openid", openId);
            attributes.put("username", user.getUsername());

            // 权限列表应该根据你的业务逻辑来填充，这里为了简化，我们只赋予基础角色权限
            List<String> authorities = Collections.singletonList("ROLE_USER");
            return new LoginUser(user, authorities, attributes);
        }

        // 如果有其他OAuth2提供商，可以在这里添加逻辑
        throw new OAuth2AuthenticationException("不支持的OAuth2提供商: " + registrationId);
    }

    private String getOpenId(OAuth2UserRequest userRequest) {
        String userInfoUri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String url = userInfoUri + "?access_token=" + accessToken;

        String response = restTemplate.getForObject(url, String.class);
        // QQ的响应是 JSONP 格式: callback( {"client_id":"YOUR_APP_ID","openid":"YOUR_OPENID"} );
        // 我们需要从中提取出 openid
        String openId = response.substring(response.indexOf("\"openid\":\"") + 10, response.indexOf("\"}"));
        return openId;
    }

    private User createNewQqUser(String openId) {
        User newUser = new User();
        String username = "qq_" + openId;
        newUser.setUsername(username);
        // OAuth2用户通常没有密码，我们可以设置一个随机的或者固定的不可登录密码
        newUser.setPassword("OAUTH2_USER_" + System.currentTimeMillis());
        newUser.setEnabled(true);
        userService.save(newUser);

        // 分配默认角色
        Role userRole = roleService.getOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Role>().eq(Role::getCode, "ROLE_USER"));
        if (userRole != null) {
            UserRole ur = new UserRole();
            ur.setUserId(newUser.getId());
            ur.setRoleId(userRole.getId());
            userRoleService.save(ur);
        }
        return newUser;
    }
} 