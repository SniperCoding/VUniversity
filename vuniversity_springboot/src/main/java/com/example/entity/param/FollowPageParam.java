package com.example.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhang
 * @date 2022/06/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowPageParam {
    // 用户id
    int userId;
    // 页码
    int pageNum;
    // 每页数量
    int pageSize;
}
