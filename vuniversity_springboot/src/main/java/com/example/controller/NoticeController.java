package com.example.controller;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.param.NoticePageParam;
import com.example.service.NoticeService;
import com.example.util.ConstantUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("notice")
@PreAuthorize("isAuthenticated()")  // 只要登录才有通知
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation("查询用户点赞/评论/关注下最新的通知")
    @GetMapping("/notices")
    public Result getLatestNotice() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("like", noticeService.getLatestNotice(user.getId(), ConstantUtil.NOTICE_LIKE));
        map.put("comment", noticeService.getLatestNotice(user.getId(), ConstantUtil.NOTICE_COMMENT));
        map.put("follow", noticeService.getLatestNotice(user.getId(), ConstantUtil.NOTICE_FOLLOW));
        return Result.ok();
    }

    @ApiOperation("获取所有未读通知数量")
    @GetMapping("/allUnreadCount")
    public Result getAllConversations() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.ok(noticeService.getNoticeUnreadCount(user.getId(), -1));
    }

    @ApiOperation("分页查询用户点赞/评论/关注下的通知列表")
    @PostMapping("/allNotices")
    public Result getAllNotices(@RequestBody NoticePageParam param) {
        noticeService.getAllNotices(param);
        return Result.ok();
    }
}
