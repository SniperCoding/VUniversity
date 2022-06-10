package com.example.controller;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.param.MessageParam;
import com.example.entity.param.PageParam;
import com.example.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/message")
@PreAuthorize("isAuthenticated()")  // 只有登录才能查看私信相关功能
public class MessageController {
    @Autowired
    private MessageService messageService;

    @ApiOperation("显示会话列表")
    @PostMapping("/conversations")
    public Result getAllConversations(@RequestBody PageParam param) {
        return Result.ok(messageService.getConversations(param));
    }

    @ApiOperation("获取所有未读消息数量")
    @GetMapping("/allUnreadCount")
    public Result getAllConversations() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.ok(messageService.getAllUnreadMessageCount(user.getId()));
    }

    @ApiOperation("获取会话详情")
    @PostMapping("/details")
    public Result getMessagesDetailsByConversationId(@RequestBody PageParam param, @RequestParam String conversationId) {
        Map<String, Object> details = messageService.getAllMessagesByConversationId(param, conversationId);
        if (details == null) {
            return Result.error();
        }
        return Result.ok(details);
    }

    @ApiOperation("添加一条消息")
    @PostMapping("/save")
    public Result savePost(@RequestBody MessageParam messageParam) {
        try {
            messageService.saveMessage(messageParam);
        } catch (Exception e) {
            return Result.error(400, "收信人不存在！");
        }
        return Result.ok();
    }
}
