package com.backend.movies.config;

import com.backend.movies.Middlewares.AuthFilter;
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

/**
 * @author luisaAran
 * Configuración de seguridad de la aplicación
 * Define las reglas de acceso a los endpoints y
 * los filtros de seguridad aplicados a cada peticion.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthFilter authFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor de la clase, que inyecta las dependencias
     * @param authFilter
     * @param userDetailsService
     */
    public SecurityConfig(AuthFilter authFilter, UserDetailsService userDetailsService){
        this.authFilter = authFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Bean que define la cadena de filtros de seguridad
     * y las reglas de acceso a los endpoints
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                /*
                    Deshabilita la protección CSRF, ya que la aplicación no depende
                    de cookies para la autenticación (usa tokens JWT).
                 */
                .csrf(AbstractHttpConfigurer::disable)
                /*
                    Define las reglas de autorización para los distintos endpoints.
                    - Permite el acceso sin autenticación a los endpoints de creación
                      de usuarios y generación de tokens.
                    - Restringe el acceso a ciertos endpoints según el rol del usuario
                      (USER o ADMIN).
                    - Requiere autenticación para cualquier otra petición.
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/generateToken").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/movies").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                /*
                    Configura la gestión de sesiones para que sea sin estado (stateless),
                    ya que la autenticación se maneja mediante tokens JWT.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /*
                    Configura el filtro de autenticación personalizado (AuthFilter)
                 */
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Bean que define el proveedor de autenticación,
     * que utiliza un UserDetailsService para cargar los
     * detalles del usuario y un PasswordEncoder para verificar la contraseña.
     * @param passwordEncoder
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Bean que expone el AuthenticationManager necesario
     * para el proceso de autenticación.
     * @param config
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

}
