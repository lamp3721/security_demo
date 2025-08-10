package org.example.filter;

import lombok.RequiredArgsConstructor;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器，在每个请求中验证JWT Token的有效性。
 * 该过滤器继承自OncePerRequestFilter，确保每个请求只被过滤一次。
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * 过滤器的核心逻辑。
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 从请求头获取 "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 如果请求头为空或不是以 "Bearer " 开头，则直接放行
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 提取JWT（去掉 "Bearer " 前缀）
        jwt = authHeader.substring(7);
        // 从JWT中解析出用户名
        username = jwtUtil.extractUsername(jwt);

        // 如果用户名不为空，且当前安全上下文中没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 根据用户名加载用户详细信息
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 验证Token是否有效
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // 创建一个已认证的Authentication对象
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                // 将请求的详细信息设置到Authentication对象中
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // 将Authentication对象设置到安全上下文中，表示当前用户已认证
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 继续执行过滤器链中的下一个过滤器
        filterChain.doFilter(request, response);
    }
} 