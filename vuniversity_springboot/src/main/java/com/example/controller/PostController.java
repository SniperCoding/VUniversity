package com.example.controller;

import com.example.entity.Post;
import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.param.PostPageParam;
import com.example.entity.param.PostParam;
import com.example.entity.vo.PostUserVO;
import com.example.service.PostService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @ApiOperation("添加一条帖子")
    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public Result savePost(@RequestBody PostParam postParam, HttpServletRequest request) {
        String insertPostToken = request.getHeader("InsertPostToken");
        boolean success = postService.savePost(postParam, insertPostToken);
        return success ? Result.ok() : Result.error(400, "表单重复提交或其他错误！");
    }

    @ApiOperation("获取添加帖子的token")
    @GetMapping("/getToken")
    @PreAuthorize("isAuthenticated()")
    public Result getInsertPostToken() {
        String insertPostToken = postService.getInsertPostToken();
        return Result.ok(insertPostToken);
    }

    @ApiOperation("根据帖子id获取帖子及作者信息")
    @GetMapping("/{postId}")
    public Result getPostUserVoById(@PathVariable("postId") int id) {
        PostUserVO postUserVO = postService.getPostById(id);
        return Result.ok(postUserVO);
    }

    @ApiOperation("修改帖子内容")
    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public Result updatePost(@RequestBody Post post) {
        postService.updatePost(post);
        return Result.ok();
    }

    @ApiOperation("删除帖子")
    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public Result deletePostById(@PathVariable("postId") int id) {
        postService.delete(id);
        return Result.ok();
    }
}
