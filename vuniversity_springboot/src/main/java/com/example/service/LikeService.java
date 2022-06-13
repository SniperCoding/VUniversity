package com.example.service;


public interface LikeService {
    /**
     * 给帖子/评论点赞或取消点赞（注意给帖子/评论点赞的同时，被赞用户的点赞数也相应变化）
     * @param fromUserId 点赞用户的id
     * @param type  被点赞的类型 0帖子/1评论
     * @param id    被点赞的帖子或评论的id
     * @param toUserId  被点赞用户的id
     */
    void likePostOrComment(int fromUserId, int type, long id, int toUserId);

    /**
     * 查询用户是否对帖子/评论进行了点赞
     *
     * @param fromUserId 用户的id
     * @param type       类型 0帖子/1评论
     * @param id         帖子或评论的id
     * @return true表示已点赞，false表示未点赞
     */
    boolean isLike(int fromUserId, int type, long id);

    /**
     * 获取帖子/评论的点赞数
     *
     * @param type 类型 0帖子/1评论
     * @param id   帖子或评论的id
     * @return
     */
    long getPostOrCommentLikeCount(int type, long id);

    /**
     * 获取用户获得的总点赞数
     *
     * @param userId
     * @return
     */
    long getUserLikeCount(int userId);
}
