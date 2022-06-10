package com.example.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {
    // 页码
    private Integer pageNum;
    // 每页数量
    private Integer pageSize;
}
