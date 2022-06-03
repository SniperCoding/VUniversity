package com.example.service.impl;

import com.example.entity.Post;
import com.example.entity.User;
import com.example.entity.param.PostPageParam;
import com.example.entity.vo.PostUserVO;
import com.example.mapper.PostMapper;
import com.example.service.PostService;
import com.example.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserService userService;

    @Override
    public List<PostUserVO> findAll(PostPageParam postPageParam) {
        // 1.分页条件和排序方式
        Integer pageNum = postPageParam.getPageNum();
        Integer pageSize = postPageParam.getPageSize();
        Integer orderMode = postPageParam.getOrderMode();
        // 2.执行分页
        PageHelper.startPage(pageNum,pageSize);
        // 3.查询数据库，查询结果就是分页后的数据了
        Integer userId = postPageParam.getUserId();
        List<Post> posts = postMapper.findAll(orderMode, userId == null ? 0 : userId);
        // 4.根据帖子查询其作者信息，并将帖子信息和作者信息封装到PostUserVO中
        List<PostUserVO> postUserVos = new ArrayList<>();
        for(Post post:posts){
            // 4.1 根据id查询用户信息
            User user = userService.getUserById(post.getUserId());
            PostUserVO postUserVo = new PostUserVO();
            // 4.2 复制帖子信息
            BeanUtils.copyProperties(post,postUserVo);
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
}
