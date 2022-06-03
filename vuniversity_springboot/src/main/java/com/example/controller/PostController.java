package com.example.controller;

import com.example.entity.Result;
import com.example.entity.param.PostPageParam;
import com.example.service.PostService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @ApiOperation("分页查询帖子")
    @PostMapping("/findAll")
    public Result findAll(@RequestBody PostPageParam postPageParam) {
        return Result.ok(postService.findAll(postPageParam));
    }

    @ApiOperation("查询用户发帖数量")
    @GetMapping("/count")
    public Result getPostCount(@RequestParam(defaultValue = "0") int userId) {
        return Result.ok(postService.getPostCount(userId));
    }
}
