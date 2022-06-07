package com.example.config;

import com.example.security.*;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity  // 此注解已经包括了@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启对方法授权的支持
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 注入 UserDetailService实现类，用于获取用户信息
    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Spring Security 提供的使用哈希算法结合盐值（盐值即一个安全随机数）加密器，
     * 该类对同一明文每次加密都不一样，哈希又是一种不可逆算法，
     * 所以密码认证时需要使用相同的方式对待校验的明文进行加密，然后比较这两个密文来进行验证。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomizeLoginSuccessHandler loginSuccessHandler;  // 注入认证成功的处理类

    @Autowired
    private CustomizeLoginFailureHandler loginFailureHandler;  // 注入认证失败的处理类

    @Autowired
    private CustomizeLogoutSuccessHandler logoutSuccessHandler;  // 注入登出的处理类

    @Autowired
    private CustomizeAuthenticationEntryPoint authenticationEntryPoint;  // 注入未认证访问无权限资源的处理类

    @Autowired
    private CustomizeAccessDeniedHandler accessDeniedHandler; // 注入已认证访问无权限资源的处理类

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter; // 注入JWT过滤器

    // 配置用户信息的来源以及密码加密规则
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl)   // 获取用户信息
                .passwordEncoder(passwordEncoder());  // 密码验证方式
    }

    // 配置认证和授权规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(); // 允许跨域，如果springboot已有跨域配置，自动采用 springboot 跨域配置
        http.csrf().disable(); // 禁用csrf模式
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 基于 token,不需要 session
        // 【重要】在 UsernamePasswordAuthenticationFilter过滤器之前添加jwt过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http
                // 登入，允许所有用户【写完这个就不需要再自定义/login接口了】
                .formLogin()
                .loginProcessingUrl("/login")  // 设置前端请求登录的接口POST，默认即为/login
                .usernameParameter("username")  // 定义登录时，用户名的参数名，默认即为 username
                .passwordParameter("password")  // 定义登录时，密码的参数名，默认即为 password
                .successHandler(loginSuccessHandler)   // 认证成功处理类，也可以使用匿名内部类/lambda表达式在这里直接写，就不需要单独写前面的一些处理类了
                .failureHandler(loginFailureHandler)    // 认证失败处理类
                .permitAll()  // 登录相关访问地址一律放行
                //登出，允许所有用户【写完这个就不需要再自定义/logout接口了】
                .and().logout()
                .logoutUrl("/logout")   //退出地址，默认即为/logout
                .logoutSuccessHandler(logoutSuccessHandler)    // 注销处理类
                .permitAll()
                // 异常处理
                .and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)  // 未认证请求无权限资源处理类
                .accessDeniedHandler(accessDeniedHandler)   // 已认证访问无权限资源处理类
                .and().authorizeRequests();
    }
}
