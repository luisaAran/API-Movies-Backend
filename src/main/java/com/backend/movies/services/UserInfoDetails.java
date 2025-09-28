package com.backend.movies.services;

import com.backend.movies.models.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements org.springframework.security.core.userdetails.UserDetails {
    private String username;
    private String password;
    private GrantedAuthority authority;
    public UserInfoDetails(UserInfo userInfo){
        this.username = userInfo.getEmail();
        this.password = userInfo.getPassword();
        this.authority = new SimpleGrantedAuthority(userInfo.getRole().name());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return true;
    }
}
