package com.example.security;

import com.example.entity.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        // 1.返回json数据
        String result = new ObjectMapper().writeValueAsString(Result.error(403,"权限不足！"));
        // 2.处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        // 3.使用HttpServletResponse中返回给前台
        response.getWriter().write(result);
    }
}
