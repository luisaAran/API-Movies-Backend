package com.backend.movies.config;

import com.backend.movies.Middlewares.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthFilter authFilter;
    private final UserDetailsService userDetailsService;
    public SecurityConfig(AuthFilter authFilter, UserDetailsService userDetailsService){
        this.authFilter = authFilter;
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "api/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/users/generateToken").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/users/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "api/movies").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "api/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "api/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "api/movies/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

}
