package com.example.mapper;

public interface MailMapper {
    /**
     * @param mail
     * @return 返回0表示未被注册，1表示已被注册
     */
    int existMail(String mail);
}
