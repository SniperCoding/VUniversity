package com.example.mapper;

import com.example.entity.Post;

import java.util.List;

public interface PostMapper {
    /**
     * 查询所有帖子信息（如果传入UserId，则只查询用户的全部帖子）
     *
     * @param orderMode 排序方式： 0：按时间排序，1：按热度排序
     * @param userId 用户id
     * @return 所有帖子信息
     */
    List<Post> findAll(int orderMode, int userId);

    /**
     * 查询所有帖子数量（如果传入用户id，则查询用户的所有帖子数量，否则查询全部）
     * @param userId 用户id
     * @return
     */
    int getPostCount(int userId);

    /**
     * 插入一条帖子
     * @param post
     * @return
     */
    int insertPost(Post post);

    /**
     * 根据id查询帖子
     * @param id
     * @return
     */
    Post getPostById(int id);

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
}
