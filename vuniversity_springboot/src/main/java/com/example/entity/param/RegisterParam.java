package com.example.entity.param;

import lombok.Data;

@Data
public class RegisterParam {
    private String username;
    private String password;
    private String email;
    private String code;
}
