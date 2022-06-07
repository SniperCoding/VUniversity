package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    // 主键
    private Integer id;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 邮箱
    private String email;
    // 头像
    private String avatar;
    // 状态：0正常 1禁用
    private Integer state;
    // 注册时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerTime;

    // 用户的角色列表，我们的表中无角色字段，而是由另一张表维护着角色与用户关系，那就需要在查询的时候，
    // 额外查出角色信息，并且要确保在 getAuthorities()能获取到对应的角色信息
    @JsonIgnore
    private List<Role> roles;

    /**
     * 【重要】返回分配给用户的角色列表，将每个角色转换为 SimpleGrantedAuthority 返回，
     * @return
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return state==0;
    }
}
