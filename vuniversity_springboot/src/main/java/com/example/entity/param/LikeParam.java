package com.example.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeParam {

    // 点赞用户的id
    Integer fromUserId;

    // 被点赞的类型 0帖子/1评论
    Integer type;

    // 被点赞的帖子或评论的id
    Long id;

    // 被点赞用户的id
    Integer toUserId;
}
