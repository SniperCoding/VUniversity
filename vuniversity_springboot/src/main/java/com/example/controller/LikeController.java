package com.example.controller;

import com.example.entity.Result;
import com.example.entity.param.LikeParam;
import com.example.service.LikeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/like")
@PreAuthorize("isAuthenticated()")
@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;

    @ApiOperation("点赞或取消点赞")
    @PostMapping("/like")
    public Result like(@RequestBody LikeParam param) {
        // 进行点赞/取消点赞
        likeService.likePostOrComment(param.getFromUserId(), param.getType(), param.getId(), param.getToUserId());
        // 查询点赞数量和点赞状态
        long likeCount = likeService.getPostOrCommentLikeCount(param.getType(), param.getId());
        boolean likeState = likeService.isLike(param.getFromUserId(), param.getType(), param.getId());
        // 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeCount);
        result.put("likeState", likeState);
        return Result.ok(result);
    }
}
