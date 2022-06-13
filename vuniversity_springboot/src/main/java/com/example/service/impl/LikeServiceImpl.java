package com.example.service.impl;

import com.example.service.LikeService;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public void likePostOrComment(int fromUserId, int type, long id, int toUserId) {
        // 1.获取存储在Redis中对帖子/评论点赞的键
        String likeKey = type == 0 ? RedisKeyUtil.getLikePost((int) id) : RedisKeyUtil.getLikeComment(id);
        // 2.获取存储在Redis中对用户点赞的键
        String likeUserKey = RedisKeyUtil.getLikeUser(toUserId);
        // 2.判断是点赞还是取消赞（即判断点赞者id是否出现在likeKey的set集合中）
        Boolean liked = redisTemplate.opsForSet().isMember(likeKey, fromUserId);
        // 2.1 如果已经点赞过了，即取消点赞，则将点赞者id从set集合中取出，并将被点赞者的点赞数量减1
        if (liked) {
            redisTemplate.opsForSet().remove(likeKey, fromUserId);
            redisTemplate.opsForValue().decrement(likeUserKey);
        } else {  // 2.2 如果点赞，则将点赞者id存放在set集合中，并将被点赞者的点赞数量加1
            redisTemplate.opsForSet().add(likeKey, fromUserId);
            redisTemplate.opsForValue().increment(likeUserKey);
        }
    }

    @Override
    public boolean isLike(int fromUserId, int type, long id) {
        //  1.获取存储在Redis中对帖子/评论点赞的键
        String likeKey = type == 0 ? RedisKeyUtil.getLikePost((int) id) : RedisKeyUtil.getLikeComment(id);
        // 2.判断是点赞还是取消赞（即判断点赞者id是否出现在likeKey的set集合中）
        return redisTemplate.opsForSet().isMember(likeKey, fromUserId);
    }

    @Override
    public long getPostOrCommentLikeCount(int type, long id) {
        // 1.获取存储在Redis中对帖子/评论点赞的键
        String likeKey = type == 0 ? RedisKeyUtil.getLikePost((int) id) : RedisKeyUtil.getLikeComment(id);
        // 2.获取帖子/评论的点赞数量
        return redisTemplate.opsForSet().size(likeKey);
    }

    @Override
    public long getUserLikeCount(int userId) {
        // 1.获取存储在Redis中对用户点赞的键
        String likeUserKey = RedisKeyUtil.getLikeUser(userId);
        // 2.获取用户获得的总赞数
        Long size = (Long) redisTemplate.opsForValue().get(likeUserKey);
        return size == null ? 0L : size;
    }
}
