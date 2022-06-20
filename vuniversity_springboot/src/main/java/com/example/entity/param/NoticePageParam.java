package com.example.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticePageParam {
    // 用户id
    private Integer userId;
    // 通知类型
    private Integer type;
    // 页码
    private Integer pageNum;
    // 每页数量
    private Integer pageSize;
}
