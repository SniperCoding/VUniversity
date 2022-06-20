package com.example.service.impl;

import com.example.entity.Notice;
import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.vo.FollowUserVO;
import com.example.rabbitmq.RabbitmqConstant;
import com.example.rabbitmq.RabbitmqListener;
import com.example.service.FollowService;
import com.example.service.NoticeService;
import com.example.service.UserService;
import com.example.util.ConstantUtil;
import com.example.util.RedisKeyUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
//    @Autowired
//    private NoticeService noticeService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Result follow(int fromUserId, int toUserId) {
        // 1.判断是否是当前用户操作
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != fromUserId) {
            return Result.error(401, "非法操作！");
        }
        // 2.判断是关注还是取关
        boolean followed = isFollowed(fromUserId, toUserId);
        // 3.获取存在redis中的key
        String followeeKey = RedisKeyUtil.getFollowee(toUserId);
        String followerKey = RedisKeyUtil.getFollower(fromUserId);
        // 4 执行操作
        if (followed) {  // 已关注，现在是取关操作
            // 发布系统通知
            Notice notice = new Notice(null, ConstantUtil.NOTICE_FOLLOW, toUserId, fromUserId, null, ConstantUtil.Message_UNREAD, new Date());
//            noticeService.saveNotice(notice);
            rabbitTemplate.convertAndSend(RabbitmqConstant.NOTICE_EXCHANGE, RabbitmqConstant.NOTICE_KEY, notice);
            // 关注者少了一个关注用户
            redisTemplate.opsForZSet().remove(followeeKey, toUserId);
            // 被关注者少了一个粉丝
            redisTemplate.opsForZSet().remove(followerKey, fromUserId);

        } else {  // 未关注，现在是关注操作
            // 关注者多了一个关注用户
            redisTemplate.opsForZSet().add(followeeKey, toUserId, System.currentTimeMillis());
            // 被关注者多了一个粉丝
            redisTemplate.opsForZSet().add(followerKey, fromUserId, System.currentTimeMillis());
        }
        // 5.返回结果
        return Result.ok();
    }

    @Override
    public boolean isFollowed(int fromUserId, int toUserId) {
        // 1.获取存在redis中的key
        String followeeKey = RedisKeyUtil.getFollower(fromUserId);
        // 2.判断在用户关注列表中,toUserId的分数，如果分数为空，则说明不在关注列表中，反之则在
        Double score = redisTemplate.opsForZSet().score(followeeKey, toUserId);
        return score != null;
    }

    @Override
    public long getFollowerCount(int userId) {
        // 1.获取存在redis中的key
        String followerKey = RedisKeyUtil.getFollower(userId);
        // 2.获取粉丝列表总数
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    @Override
    public long getFolloweeCount(int userId) {
        // 1.获取存在redis中的key
        String followeeKey = RedisKeyUtil.getFollowee(userId);
        // 2.获取关注列表总数
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    @Override
    public List<FollowUserVO> getFollowers(int userId, int pageNum, int pageSize) {
        // 1.获取存在redis中的key
        String followerKey = RedisKeyUtil.getFollower(userId);
        // 2.起始索引和结束索引
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize - 1;
        // 3.分页获取粉丝用户id列表
        Set<Integer> followerIds = redisTemplate.opsForZSet().range(followerKey, start, end);
        // 4.遍历 ID 获取粉丝用户信息
        List<FollowUserVO> resultList = new ArrayList<>();
        for (Integer id : followerIds) {
            // 4.1 根据id获取用户信息
            User user = userService.getUserById(id);
            // 4.2 获取分数（关注时间）
            Double score = redisTemplate.opsForZSet().score(followerKey, id);
            // 4.3 复制属性到FollowUserVO中
            FollowUserVO followUserVO = new FollowUserVO(user.getId(), user.getUsername(), user.getAvatar(), new Date(score.longValue()));
            // 4.4 添加到粉丝列表中
            resultList.add(followUserVO);
        }
        return resultList;
    }

    @Override
    public List<FollowUserVO> getFollowees(int userId, int pageNum, int pageSize) {
        // 1.获取存在redis中的key
        String followeeKey = RedisKeyUtil.getFollowee(userId);
        // 2.起始索引和结束索引
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize - 1;
        // 3.分页获取用户关注id列表
        Set<Integer> followeeIds = redisTemplate.opsForZSet().range(followeeKey, start, end);
        // 3.遍历 ID 获取关注信息
        List<FollowUserVO> resultList = new ArrayList<>();
        for (Integer id : followeeIds) {
            // 3.1 根据id获取用户信息
            User user = userService.getUserById(id);
            // 3.2 获取分数（关注时间）
            Double score = redisTemplate.opsForZSet().score(followeeKey, id);
            // 3.3 复制属性到FollowUserVO中
            FollowUserVO followUserVO = new FollowUserVO(user.getId(), user.getUsername(), user.getAvatar(), new Date(score.longValue()));
            // 3.3 添加到关注列表中
            resultList.add(followUserVO);
        }
        return resultList;
    }
}
