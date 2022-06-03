package com.example.service;

public interface MailService {
    /**
     * 判断邮箱是否已被注册
     *
     * @param mail
     * @return
     */
    public boolean isMailExist(String mail);

    /**
     * 发送注册验证码
     *
     * @param to 收件人邮箱
     * @return 返回true一切正常，false表示邮箱已被注册或者邮箱格式不合法
     */
    public boolean sendMail(String to) throws Exception;
}
