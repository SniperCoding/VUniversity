package com.example.util;

import java.util.UUID;

public class RedisKeyUtil {
    private static final String PREFIX_MAIL_REGISTER_CODE = "mail:register:code:";  // 邮箱验证码前缀
    private static final String PREFIX_INSERT_POST_TOKEN = "insert:post:token:";   // 添加文章，获取token令牌
    private static final String PREFIX_KAPTCHA_CODE ="kaptcha:code:";  // 邮箱验证码前缀
    private static final String PREFIX_LIKE_POST = "like:post:";   // 帖子的赞
    private static final String PREFIX_LIKE_COMMENT = "like:comment:";   // 评论的赞
    private static final String PREFIX_LIKE_USER = "like:user:";   // 用户的赞
    /**
     * 获取Redis中存放注册验证码的键
     *
     * @param email 用户邮箱
     * @return
     */
    public static String getRegisterCode(String email) {
        return PREFIX_MAIL_REGISTER_CODE + email;
    }

    /**
     * 获取Redis中存放添加文章token的键，形式为:user:用户id
     *
     * @param uuid
     * @return
     */
    public static String getInsertPostToken(String uuid) {
        return PREFIX_INSERT_POST_TOKEN + uuid;
    }

    /**
     * 获取Redis中存放登录验证码的键，形式为 kaptcha:code:UUID
     * @return
     */
    public static String getKaptchaCode(){
        return PREFIX_KAPTCHA_CODE  + UUID.randomUUID();
    }

    /**
     * 获取Redis中存放帖子点赞数的键，形式为like:post:帖子id
     *
     * @param postId
     * @return
     */
    public static String getLikePost(int postId) {
        return PREFIX_LIKE_POST + postId;
    }

    /**
     * 获取Redis中存放评论点赞数的键，形式为like:comment:评论id
     *
     * @param commentId
     * @return
     */
    public static String getLikeComment(long commentId) {
        return PREFIX_LIKE_COMMENT + commentId;
    }

    /**
     * 获取Redis中存放用户总点赞数的键，形式为like:user:用户id
     *
     * @param userId
     * @return
     */
    public static String getLikeUser(int userId) {
        return PREFIX_LIKE_USER + userId;
    }
}
