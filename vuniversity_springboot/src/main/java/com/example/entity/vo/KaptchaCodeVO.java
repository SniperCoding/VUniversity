package com.example.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaptchaCodeVO {
    private String verifyKey;  // 验证码的key
    private String img;         // 验证码图片（Base64编码）
}
