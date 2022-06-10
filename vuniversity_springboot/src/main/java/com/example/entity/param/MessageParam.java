package com.example.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageParam {
    /**
     * 发信人id
     */
    private Integer fromUserId;
    /**
     * 收信人id
     */
    private Integer toUserId;
    /**
     * 私信内容
     */
    private String content;
}
