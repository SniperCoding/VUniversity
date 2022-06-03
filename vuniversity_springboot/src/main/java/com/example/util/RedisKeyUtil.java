package com.example.util;

public class RedisKeyUtil {
    private static final String PREFIX_MAIL_REGISTER_CODE = "mail:register:code:";  // 邮箱验证码前缀

    /**
     * 获取Redis中存放注册验证码的键
     *
     * @param email 用户邮箱
     * @return
     */
    public static String getRegisterCode(String email) {
        return PREFIX_MAIL_REGISTER_CODE + email;
    }
}
