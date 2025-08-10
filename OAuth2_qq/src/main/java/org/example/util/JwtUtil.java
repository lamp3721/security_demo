package org.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类，负责生成、解析和验证Token。
 */
@Component
public class JwtUtil {

    /**
     * 从配置文件中注入的JWT密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 从配置文件中注入的JWT过期时间（秒）
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据密钥生成的Key对象，用于签名和验证
     */
    private Key key;

    /**
     * 构造函数，在注入secret后立即初始化Key对象。
     * @param secret JWT密钥
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 根据用户信息生成JWT。
     * @param userDetails 包含用户详细信息的对象
     * @return 生成的JWT字符串
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * 创建JWT的核心方法。
     * @param claims 附加信息
     * @param subject 主题，通常是用户名
     * @return JWT字符串
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从Token中提取指定的声明（Claim）。
     * @param token JWT
     * @param claimsResolver 用于提取声明的函数
     * @return 提取出的声明
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析JWT并获取所有的声明（Claims）。
     * @param token JWT
     * @return 所有的声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * 从Token中提取用户名。
     * @param token JWT
     * @return 用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从Token中提取过期时间。
     * @param token JWT
     * @return 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 检查Token是否已过期。
     * @param token JWT
     * @return 如果已过期则返回true
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 验证Token是否有效。
     * @param token JWT
     * @param userDetails 用户详细信息对象
     * @return 如果Token有效且与用户信息匹配则返回true
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
} 