package com.example.service;

import com.example.entity.param.PostPageParam;
import com.example.entity.vo.PostUserVO;

import java.util.List;

public interface PostService {

    /**
     * 分页查询所有帖子信息及帖子作者部分信息
     * @param postPageParam 查询参数，包括分页参数和排序方式
     * @return
     */
    List<PostUserVO> findAll(PostPageParam postPageParam);

    /**
     * 查询所有帖子数量（如果传入用户id，则查询用户的所有帖子数量，否则查询全部）
     * @param userId 用户id
     * @return
     */
    int getPostCount(int userId);
}
