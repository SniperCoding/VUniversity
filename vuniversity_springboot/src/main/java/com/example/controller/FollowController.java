package com.example.controller;

import com.example.entity.Result;
import com.example.entity.param.FollowPageParam;
import com.example.service.FollowService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @ApiOperation("关注或取关")
    @PutMapping("/follow")
    @PreAuthorize("isAuthenticated()")
    public Result follow(@RequestParam int fromUserId, @RequestParam int toUserId) {
        return followService.follow(fromUserId, toUserId);
    }

    @ApiOperation("获取关注状态")
    @GetMapping("/status")
    public Result getStatus(@RequestParam int fromUserId, @RequestParam int toUserId) {
        boolean followed = followService.isFollowed(fromUserId, toUserId);
        return Result.ok(followed);
    }

    @ApiOperation("分页获取粉丝列表")
    @PostMapping("/followerList")
    public Result getFollowerList(@RequestBody FollowPageParam param) {
        return Result.ok(followService.getFollowers(param.getUserId(), param.getPageNum(), param.getPageSize()));
    }

    @ApiOperation("分页获取关注列表")
    @PostMapping("/followeeList")
    public Result getFolloweeList(@RequestBody FollowPageParam param) {
        return Result.ok(followService.getFollowees(param.getUserId(), param.getPageNum(), param.getPageSize()));
    }

    @ApiOperation("获取粉丝数量")
    @GetMapping("/followerCount")
    public Result getFollowerCount(@RequestParam int userId) {
        return Result.ok(followService.getFollowerCount(userId));
    }

    @ApiOperation("获取关注数量")
    @GetMapping("/followeeCount")
    public Result getFolloweeCount(@RequestParam int userId) {
        return Result.ok(followService.getFolloweeCount(userId));
    }
}
