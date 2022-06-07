package com.example.security;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.vo.UserVO;
import com.example.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomizeLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.expiration}")
    private Long expiration; // token过期时间 （ 60*60*24）1天后失效

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        // 1.获取用户信息
        User user = (User) authentication.getPrincipal();
        // 2.生成双token
        String accessToken = jwtTokenUtil.generateToken(user, expiration);
        String refreshToken = jwtTokenUtil.generateToken(user, expiration * 2);
        // 3.封装为json数据
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        map.put("user", userVO);
        String result = new ObjectMapper().writeValueAsString(Result.ok(map));
        // 4.处理编码方式，防止中文乱码的情况
        httpServletResponse.setContentType("text/json;charset=utf-8");
        // 5.使用 HttpServletResponse中返回给前端
        httpServletResponse.getWriter().write(result);
    }
}
