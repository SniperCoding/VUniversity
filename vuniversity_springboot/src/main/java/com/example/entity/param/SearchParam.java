package com.example.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchParam {
    /**
     * 搜索关键字
     */
    private String key;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页展示多少条
     */
    private Integer pageSize;
    /**
     * 排序方式
     */
    private String sortBy;
}
