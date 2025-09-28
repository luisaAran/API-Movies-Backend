package com.backend.movies.Middlewares;

import com.backend.movies.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro de seguridad que intercepta las peticiones HTTP con el fin
 * de validar el token JWT incluido en la cabecera Authorization
 */
@Component
public class AuthFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * Constructor del filtro, que inyecta las dependencias necesarias
     * para generar las validaciones.
     * @param userDetailsService
     * @param jwtService
     */
    @Autowired
    public AuthFilter(UserDetailsService userDetailsService, JwtService jwtService){
        this. userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * Método que intercepta las peticiones HTTP y valida el token JWT,
     * mediante una cadena de filtros.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        /*
         * Si se obtiene un nombre de usuario, y este todavia no está autenticado, procede
         * a buscar dicho nombre de usuario en la base de datos de la App
         */
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            /*
                Valida que el token tenga una firma valida, no esté expirado, y sea perteneciente
                al mismo usario que los datos recuperados.
             */
            if(jwtService.validateToken(token, userDetails)){
                /*
                    Crea un objeto de autenticacion con base a los detalles del usuario
                    y lo añade al contexto de seguridad de Spring

                    Se omite el parametro de credentials (null) porque ya se ha validado el token, y
                    por ende, la contraseña del usuario
                 */
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                /*
                    Añade detalles adicionales de la petición al objeto de autenticación
                 */
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                /*
                    Finalmente, se establece el objeto de autenticación en el contexto de seguridad
                    de Spring, lo que indica que el usuario ha sido autenticado correctamente
                 */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
