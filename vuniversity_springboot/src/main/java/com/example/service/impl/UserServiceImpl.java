package com.example.service.impl;

import com.example.entity.Result;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.param.RegisterParam;
import com.example.entity.vo.UserVO;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.util.JwtTokenUtil;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.expiration}")
    private Long expiration; // token过期时间

    @Value("${vuniversity.default-avatar}")
    private String defaultAvatar;

    @Override
    public User getUserById(int userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public User getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    @Override
    public Result saveUser(RegisterParam registerParam) {
        // 判断用户名和邮箱是否已被注册
        if (getUserByName(registerParam.getUsername()) != null) {
            return Result.error(400, "用户名已被注册");
        }
        // 判断注册邮箱验证码是否正确
        String registerCode = RedisKeyUtil.getRegisterCode(registerParam.getEmail());
        String code = (String) redisTemplate.opsForValue().get(registerCode);
        if (!Objects.equals(code, registerParam.getCode())) {
            return Result.error(400, "验证码错误！");
        }
        // 设置一些初始默认值
        User user = new User();
        BeanUtils.copyProperties(registerParam, user);
        user.setAvatar(defaultAvatar);
        user.setState(0);
        user.setRegisterTime(new Date());
        // 密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // 插入到数据库中
        userMapper.insertUser(user);
        return Result.ok();
    }

    @Override
    public Result getNewTokenByRefreshToken(String token) {
        // 获取用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 生成双token，并返回给前端
        String accessToken = jwtTokenUtil.generateToken(user, expiration);
        String refreshToken = jwtTokenUtil.generateToken(user, expiration * 2);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return Result.ok(map);
    }

    @Override
    public UserVO getUserVOById(int userId) {
        User user = getUserById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据用户名在数据库中查询信息（包括用户信息和角色信息）
        // 2.将查询结果封装到 UserDetails 的实现类中,即 自定义的User实体类
        User user = userMapper.getUserByName(username);
        if (user == null) {
            return null;
        }
        List<Role> roles = userMapper.getRolesByUserId(user.getId());
        user.setRoles(roles);
        return user;
    }

    @Override
    public int updatePassword(int userId, String newPassword) {
        // 密码加密
        return userMapper.updatePassword(userId, new BCryptPasswordEncoder().encode(newPassword));
    }

    @Override
    public int updateAvatar(int userId, String newAvatar) {
        return userMapper.updateAvatar(userId, newAvatar);
    }

    @Override
    public int updateState(int userId, int state) {
        return userMapper.updateState(userId, state);
    }
}
