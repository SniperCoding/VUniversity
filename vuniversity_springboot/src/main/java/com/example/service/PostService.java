package com.example.service;

import com.example.entity.Post;
import com.example.entity.param.PostPageParam;
import com.example.entity.param.PostParam;
import com.example.entity.vo.PostUserVO;

import java.util.List;

public interface PostService {

    /**
     * 分页查询所有帖子信息及帖子作者部分信息
     *
     * @param postPageParam 查询参数，包括分页参数和排序方式
     * @return
     */
    List<PostUserVO> findAll(PostPageParam postPageParam);

    /**
     * 查询所有帖子数量（如果传入用户id，则查询用户的所有帖子数量，否则查询全部）
     *
     * @param userId 用户id
     * @return
     */
    int getPostCount(int userId);

    /**
     * 插入一条帖子
     *
     * @param postParam 包含帖子标题和内容
     * @param token 令牌
     * @return
     */
    boolean savePost(PostParam postParam, String token);

    /**
     * 插入帖子前获取一个令牌
     *
     * @return
     */
    String getInsertPostToken();

    /**
     * 根据帖子id查询帖子详情和帖子所属用户详情
     * @param id
     * @return
     */
    PostUserVO getPostById(int id);

    /**
     * 修改帖子
     * @param post
     * @return
     */
    int updatePost(Post post);

    /**
     * 删除帖子
     * @param id
     * @return
     */
    int delete(int id);

    /**
     *  更新评论数量
     * @param id
     * @param commentCount
     */
    void updateCommentCount(int id,int commentCount);
}
