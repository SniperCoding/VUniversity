package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    // 主键
    private Integer id;
    // 角色名称——英文
    private String roleName;
    // 角色名称——中文
    private String roleNameZh;
}
