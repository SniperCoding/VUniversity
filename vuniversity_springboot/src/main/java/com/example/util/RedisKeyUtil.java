package com.example.util;

public class RedisKeyUtil {
    private static final String PREFIX_MAIL_REGISTER_CODE = "mail:register:code:";  // 邮箱验证码前缀
    private static final String PREFIX_INSERT_POST_TOKEN = "insert:post:token:";   // 添加文章，获取token令牌

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
}
