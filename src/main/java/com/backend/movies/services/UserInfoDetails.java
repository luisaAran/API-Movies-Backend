package com.backend.movies.services;

import com.backend.movies.models.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * @Author: luisaAran
 * Clase que implementa UserDetails para integrar la entidad UserInfo con Spring Security.
 * Proporciona los detalles necesarios para la autenticación y autorización.
 */
public class UserInfoDetails implements org.springframework.security.core.userdetails.UserDetails {
    private String username;
    private String password;
    private GrantedAuthority authority;

    /**
     * Constructor que inicializa los detalles del usuario a partir de una instancia de UserInfo.
     * @param userInfo
     */
    public UserInfoDetails(UserInfo userInfo){
        this.username = userInfo.getEmail();
        this.password = userInfo.getPassword();
        this.authority = new SimpleGrantedAuthority(userInfo.getRole().name());
    }

    /**
     * Método que devuelve las autoridades (roles) del usuario.
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }

    /**
     * Método que devuelve la contraseña del usuario.
     * @return
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Método que devuelve el nombre de usuario del usuario.
     * @return
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Método que indica si la cuenta del usuario ha expirado.
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Método que indica si la cuenta del usuario está bloqueada.
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Método que indica si las credenciales del usuario han expirado.
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Método que indica si el usuario está habilitado.
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
