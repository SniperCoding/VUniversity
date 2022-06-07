package com.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    // 定义用户名和过期时间
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret; // 密钥

    /**
     * 根据用户信息生成 token
     *
     * @param userDetails
     * @param expiration  过期时间
     * @return
     */
    public String generateToken(UserDetails userDetails, Long expiration) {
        Map<String, Object> claims = new HashMap<>(); // 准备存放 token 的容器（荷载）
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername()); // 从 security 框架 UserDetails 中获取用户名
        claims.put(CLAIM_KEY_CREATED, new Date()); // 创建时间为当前时间
        return generateToken(claims, expiration);
    }

    // 根据荷载生成 JWT token
    private String generateToken(Map<String, Object> claims, Long expiration) {
        return Jwts.builder()
                .setClaims(claims) // 荷载
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) // 过期时间
                .signWith(SignatureAlgorithm.HS512, secret)  // 加密方式
                .compact();
    }

    /**
     * 从 token 中获取登录用户名
     *
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        Claims claims = null;
        try {
            claims = getClaimsFormToken(token); // 根据 token 获取荷载
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims == null ? null : claims.getSubject(); // 通过荷载调用 getSubject 方法，获取用户名
    }

    // 从 token 中获取荷载
    private Claims getClaimsFormToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret) // 密钥
                .parseClaimsJws(token) // 签名
                .getBody();
        return claims;
    }

    /**
     * 验证 token 是否有效
     *
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username!=null && username.equals(userDetails.getUsername());
    }

    /**
     * 验证 token 是否失效
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        try {
            getClaimsFormToken(token);  // 根据 token 获取荷载
            return false;
        } catch (ExpiredJwtException e) {  // 失效会抛出此异常
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
