package com.example.entity.param;

import lombok.Data;

@Data
public class PostPageParam {
    // 页码
    private Integer pageNum;
    // 每页数量
    private Integer pageSize;
    // 排序方式：0：按时间排序，1：按热度排序
    private Integer orderMode;
    // 用户id
    private Integer userId;
}
