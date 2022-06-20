package com.example.service;

import com.example.entity.Result;
import com.example.entity.vo.FollowUserVO;

import java.util.List;

public interface FollowService {


    /**
     * 关注/取消关注
     *
     * @param fromUserId 当前用户
     * @param toUserId   当前用户关注/取消关注的用户
     * @return
     */
    Result follow(int fromUserId, int toUserId);


    /**
     * 查询fromUserID是否对toUserId进行了关注
     *
     * @param fromUserId
     * @param toUserId
     * @return true表示已关注，false表示未关注
     */
    boolean isFollowed(int fromUserId, int toUserId);

    /**
     * 获取用户的关注数
     *
     * @param userId
     * @return
     */
    long getFolloweeCount(int userId);

    /**
     * 获取用户的粉丝数
     *
     * @param userId
     * @return
     */
    long getFollowerCount(int userId);


    /**
     * 获取用户的全部关注列表(分页)
     *
     * @param userId   用户id
     * @param pageNum  页码
     * @param pageSize 每页显示几条
     * @return
     */
    List<FollowUserVO> getFollowees(int userId, int pageNum, int pageSize);

    /**
     * 获取用户的全部粉丝列表(分页)
     *
     * @param userId   用户id
     * @param pageNum  页码
     * @param pageSize 每页显示几条
     * @return
     */
    List<FollowUserVO> getFollowers(int userId, int pageNum, int pageSize);
}
