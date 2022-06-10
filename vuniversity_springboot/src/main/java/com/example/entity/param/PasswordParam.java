package com.example.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordParam {
    // 旧密码
    String oldPassword;
    // 新密码
    String newPassword;
}
