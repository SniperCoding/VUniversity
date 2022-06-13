package com.example.security;

import com.example.entity.Result;
import com.example.service.impl.UserServiceImpl;
import com.example.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userServiceImpl; // 用于根据用户名获取用户信息

    @Autowired
    private JwtTokenUtil jwtTokenUtil; // 用于生成Token、校验Token

    @Value("${jwt.header}")
    private String tokenHeader;// JWT 存储的请求头( key )

    @Autowired
    private RedisTemplate redisTemplate;

    // 前置拦截器【重要】
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 如果是登录操作，则先判断验证码是否正确
        if (request.getRequestURI().endsWith("login")) {
            String verifyKey = request.getParameter("verifyKey");
            String code = request.getParameter("code");
            // 如果没有传入verifyKey或没有传入code或传入的code不正确, 则不放行
            if (verifyKey == null || !StringUtils.hasText(code) || !code.equals((String) redisTemplate.opsForValue().get(verifyKey))) {
                //返回json数据
                Result result = Result.error(401, "验证码错误！");
                //处理编码方式，防止中文乱码的情况
                response.setContentType("text/json;charset=utf-8");
                //使用HttpServletResponse中返回给前台
                response.getWriter().write(new ObjectMapper().writeValueAsString(result));
                return;
            }
        }

        // 从请求头中获取token字符串
        String authToken = request.getHeader(this.tokenHeader);
        // token值不为空
        if (authToken != null) {
            // 如果已过期，则返回特殊状态码（用于续期功能）
            if (!request.getRequestURI().endsWith("getNewToken") && jwtTokenUtil.isTokenExpired(authToken)) {
                //返回json数据
                Result result = Result.error(410, "token已过期");
                //处理编码方式，防止中文乱码的情况
                response.setContentType("text/json;charset=utf-8");
                //使用HttpServletResponse中返回给前台
                response.getWriter().write(new ObjectMapper().writeValueAsString(result));
                return;
            }
            String username = jwtTokenUtil.getUserNameFromToken(authToken); // 从token中获取用户名
            // 用户名不为空，并且不存在上下文之中（未登录）【如果已登录过就直接放行】
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 根据用户名查询用户信息
                UserDetails user = userServiceImpl.loadUserByUsername(username);
                // 判断Token是否有效
                if (jwtTokenUtil.validateToken(authToken, user)) {
                    // 手动组装一个认证对象
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                    // 将认证信息放置到上下文中
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        // 放行
        chain.doFilter(request, response);
    }
}
