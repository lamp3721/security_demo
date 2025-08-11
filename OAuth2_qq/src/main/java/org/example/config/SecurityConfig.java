package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.filter.JwtAuthenticationTokenFilter;
import org.example.handler.AccessDeniedHandlerImpl;
import org.example.handler.AuthenticationEntryPointImpl;
import org.example.security.CustomOAuth2UserService;
import org.example.security.OAuth2LoginSuccessHandler;
import org.example.security.UserDetailsServiceImpl;
import org.example.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 * Spring Security 的核心配置类。
 * 采用了现代的、基于组件的配置方式 (告别 WebSecurityConfigurerAdapter)。
 *
 * @author 小小 (XiaoXiao)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 开启强大的方法级安全支持
@RequiredArgsConstructor
public class SecurityConfig {

    // 依赖注入我们自定义的组件
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    /**
     * 🧠 原理: 将 BCryptPasswordEncoder 注册为 Spring Bean。
     * Spring Security 会自动查找类型为 PasswordEncoder 的 Bean，并用它来校验密码。
     * BCrypt 是一种强大的、带盐的、慢哈希算法，能有效抵御彩虹表和暴力破解攻击。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 🧠 原理: 在新的 Spring Security 架构中，AuthenticationManager 不再被默认创建为 Bean。
     * 我们需要从 AuthenticationConfiguration 中显式获取并将其注册为 Bean。
     * 这样做是为了方便在登录接口等地方手动调用认证流程。
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 🚀 公共API安全过滤器链 (Public API Security Filter Chain)
     *
     * @param http HttpSecurity 配置对象
     * @return 一个专门处理公共API的 SecurityFilterChain
     *
     * 📌 设计思想:
     * 1. 使用 @Order(1) 赋予最高优先级。
     * 2. 使用 securityMatcher 指定此链只处理 "/api/auth/**" 和 "/api/setup/**" 路径的请求。
     * 3. 对匹配到的请求，完全放行 (permitAll)，且禁用CSRF和Session，因为它们是无状态的公开接口。
     */
    @Bean
    @Order(1)
    public SecurityFilterChain publicApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/auth/**", "/api/setup/**") // 仅匹配这些路径
                .csrf(csrf -> csrf.disable()) // 禁用CSRF，因为我们用JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态会话
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // 对所有匹配的请求都放行
        return http.build();
    }

    /**
     * 🚀 OAuth2 登录安全过滤器链
     *
     * @param http HttpSecurity 配置对象
     * @return 一个专门处理OAuth2登录流程的 SecurityFilterChain
     *
     * 📌 设计思想:
     * 1. 使用 @Order(2)，优先级介于公共API和私有API之间。
     * 2. securityMatcher 指定此链只处理 "/login/oauth2/**" 和 "/oauth2/**" 的请求。
     * 3. 启用 .oauth2Login() 并配置我们自定义的 userInfoEndpoint 和 successHandler。
     */
    @Bean
    @Order(2)
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/login/oauth2/**", "/oauth2/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 指定自定义的用户信息服务
                        )
                        .successHandler(oAuth2LoginSuccessHandler) // 指定自定义的成功处理器
                );
        return http.build();
    }

    /**
     * 🛡️ 私有API安全过滤器链 (Private API Security Filter Chain)
     *
     * @param http HttpSecurity 配置对象
     * @return 一个处理所有其他需要认证的API的 SecurityFilterChain
     *
     * 📌 设计思想:
     * 1. 使用 @Order(3)，优先级最低，处理所有其他请求。
     * 2. 它会处理所有未被前两个过滤器链匹配到的请求。
     * 3. 这是我们系统的主要安全屏障，配置了完整的JWT认证、授权和异常处理逻辑。
     */
    @Bean
    @Order(3)
    public SecurityFilterChain privateApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 禁用CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态会话
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // 所有其他请求都需要认证
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint) // 配置认证失败处理器
                        .accessDeniedHandler(accessDeniedHandler)         // 配置授权失败处理器
                )
                // 核心步骤：在 UsernamePasswordAuthenticationFilter 之前添加我们的JWT过滤器
                .addFilterBefore(new JwtAuthenticationTokenFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 