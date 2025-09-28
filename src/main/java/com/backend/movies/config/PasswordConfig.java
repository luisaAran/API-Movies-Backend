package com.backend.movies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author luisaAran
 * Configuración de encriptación de contraseñas
 * Usa el algoritmo BCrypt
 */
@Configuration
public class PasswordConfig {
    /**
     * Bean para el encriptador de contraseñas
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}