package com.example.service.impl;

import com.example.entity.Post;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.param.PostPageParam;
import com.example.entity.param.PostParam;
import com.example.entity.vo.PostUserVO;
import com.example.handle.GlobalException;
import com.example.mapper.PostMapper;
import com.example.service.PostService;
import com.example.service.UserService;
import com.example.util.RedisKeyUtil;
import com.example.util.SensitiveFilterUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<PostUserVO> findAll(PostPageParam postPageParam) {
        // 1.分页条件和排序方式
        Integer pageNum = postPageParam.getPageNum();
        Integer pageSize = postPageParam.getPageSize();
        Integer orderMode = postPageParam.getOrderMode();
        // 2.执行分页
        PageHelper.startPage(pageNum, pageSize);
        // 3.查询数据库，查询结果就是分页后的数据了
        Integer userId = postPageParam.getUserId();
        List<Post> posts = postMapper.findAll(orderMode, userId == null ? 0 : userId);
        // 4.根据帖子查询其作者信息，并将帖子信息和作者信息封装到PostUserVO中
        List<PostUserVO> postUserVos = new ArrayList<>();
        for (Post post : posts) {
            // 4.1 根据id查询用户信息
            User user = userService.getUserById(post.getUserId());
            PostUserVO postUserVo = new PostUserVO();
            // 4.2 复制帖子信息
            BeanUtils.copyProperties(post, postUserVo);
            // 4.3 复制用户部分信息
            postUserVo.setUsername(user.getUsername());
            postUserVo.setAvatar(user.getAvatar());
            // 4.4 存放到列表中
            postUserVos.add(postUserVo);
        }

        return postUserVos;
    }

    @Override
    public int getPostCount(int userId) {
        return postMapper.getPostCount(userId);
    }

    @Override
    public boolean savePost(PostParam postParam, String token) {
        // 如果没有发送token或者键在redis中不存在，直接返回false
        if (!StringUtils.hasText(token) || !StringUtils.hasText((String) redisTemplate.opsForValue().get(token))) {
            return false;
        }
        // 0.如果键存在，则说明是第一次提交，则将键删除
        redisTemplate.delete(token);
        // 1.获取当前用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 2. 敏感词过滤
        String title = sensitiveFilterUtil.filterKeyword(postParam.getTitle());
        String content = sensitiveFilterUtil.filterKeyword(postParam.getContent());
        // 3. 字符转义,防止XSS攻击
        title = HtmlUtils.htmlEscape(title);
        content = HtmlUtils.htmlEscape(content);
        // 4.创建帖子类
        Post post = new Post(null, user.getId(), title, content, 0, 0.0, 0, 0, new Date(), new Date());
        // 5.插入帖子
        int result = postMapper.insertPost(post);
        return result > 0;
    }

    @Override
    public String getInsertPostToken() {
        // 1.获取用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 2.获取键
        String key = RedisKeyUtil.getInsertPostToken(user.getId() + ":" + UUID.randomUUID().toString());
        // 3.存放在redis中
        redisTemplate.opsForValue().set(key, "1");
        return key;
    }

    @Override
    public PostUserVO getPostById(int id) {
        // 1.查询帖子信息
        Post post = postMapper.getPostById(id);
        if (post == null) {
            return null;
        }
        // 2.查询帖子所属用户信息
        User user = userService.getUserById(post.getUserId());
        // 3.封装
        PostUserVO postUserVO = new PostUserVO();
        BeanUtils.copyProperties(post, postUserVO);
        postUserVO.setUsername(user.getUsername());
        postUserVO.setAvatar(user.getAvatar());
        // 4.返回封装后的结果
        return postUserVO;
    }

    @Override
    public int updatePost(Post post) {
        post.setContent(sensitiveFilterUtil.filterKeyword(post.getContent()));
        post.setTitle(sensitiveFilterUtil.filterKeyword(post.getTitle()));
        post.setUpdateTime(new Date());
        return postMapper.updatePost(post);
    }

    @Override
    public int delete(int id) {
        PostUserVO post = getPostById(id);  // 查询帖子
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> roles = user.getRoles();
        boolean isAdmin = false;  // 是否为管理员
        for (Role role : roles) {
            if (role.getRoleName().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        // 不是管理员，且删除的不是自己的帖子
        if (!isAdmin && post != null && user.getId() != post.getUserId()) {
            throw new GlobalException("非法删除！");
        }
        return postMapper.delete(id);
    }
}
