package com.example.service.impl;

import com.example.entity.Result;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.param.RegisterParam;
import com.example.entity.vo.KaptchaCodeVO;
import com.example.entity.vo.UserVO;
import com.example.mapper.UserMapper;
import com.example.service.FollowService;
import com.example.service.LikeService;
import com.example.service.UserService;
import com.example.util.JwtTokenUtil;
import com.example.util.RedisKeyUtil;
import com.google.code.kaptcha.Producer;
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
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // 数学表达式验证码
    @Autowired
    private Producer captchaProducerMath;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Value("${jwt.expiration}")
    private Long expiration; // token过期时间

    @Value("${vuniversity.default-avatar}")
    private String defaultAvatar;

    @Override
    public User getUserById(int userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public UserVO getUserVOById(int userId) {
        // 1.获取用户信息
        User user = getUserById(userId);
        // 2.获取用户点赞数量
        long likeCount = likeService.getUserLikeCount(userId);
        // 3.获取用户粉丝数量
        long followerCount = followService.getFollowerCount(userId);
        // 4.获取用户关注数量
        long followeeCount = followService.getFolloweeCount(userId);
        // 5.封装信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        userVO.setLikeCount((int) likeCount);
        userVO.setFolloweeCount((int) followeeCount);
        userVO.setFollowerCount((int) followerCount);
        // 6.返回结果
        return userVO;
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


    @Override
    public KaptchaCodeVO getKaptchaCode() {
        // 1.生成验证码存放在Redis中的key
        String verifyKey = RedisKeyUtil.getKaptchaCode();  // 验证码结果在reids中对应的key
        // 2.生成验证码
        String capText = captchaProducerMath.createText(); // 生产验证码【数学表达式@计算结果】
        String capStr = capText.substring(0, capText.lastIndexOf("@")); // 获取数学表达式
        String code = capText.substring(capText.lastIndexOf("@") + 1); // 获取结果
        BufferedImage image = captchaProducerMath.createImage(capStr); // 使用生成的验证码字符串返回一个BufferedImage对象
        // 3.将验证码存放到redis中，2分钟后过期
        redisTemplate.opsForValue().set(verifyKey, code, 2, TimeUnit.MINUTES);
        // 4.将转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream(); // ByteArrayOutputStream类是在创建它的实例时，程序内部创建一个byte型别数组的缓冲区
        try {
            ImageIO.write(image, "jpg", os);  // 将BufferedImage对象直接写出指定输出流
        } catch (IOException e) {
            return null;
        }
        // 5.将结果封装到KaptchaCodeVO中返回（图片以base64进行编码）
        return new KaptchaCodeVO(verifyKey, "data:image/jpg;base64," + Base64.getEncoder().encodeToString(os.toByteArray()));
    }
}
