package com.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
@Aspect
public class ServiceLogAspect {
    // 切入点
    @Pointcut("execution(* com.example.service.impl.*.*(..))")  // 所有方法
    public void pointCut() {  // 名称任意
    }

    // 前置通知
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        // 用户[127.0.0.1]在[2022-03-17 14:06:58]访问了[com.example.service.impl.UserServiceImpl.saveUser]方法
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        // 获取request对象
        HttpServletRequest request = attributes.getRequest();
        // 获取用户ip
        String ip = getIpAddr(request);
        // 获取访问时间
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 获取访问方法全路径名
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        // 打印日志
        log.info(String.format("用户[%s],在[%s],访问了[%s].", ip, now, target));
    }

    // 获取用户IP地址
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "本地";
        }
        return ip;
    }
}
