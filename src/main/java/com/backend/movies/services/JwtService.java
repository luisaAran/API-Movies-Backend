package com.backend.movies.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author: luisaAran
 * Servicio para la generación y validación de
 * los tokens JWT de la aplicación.
 */
@Component
public class JwtService {
    /**
     * Clave secreta para la generacion y validacion de los tokens.
     */
    public static final String SECRET_KEY = "LuisaArago12345LuisaBackend8765432SpringBoot";
    public String generateToken(String email){
      Map<String, Object> claims = new HashMap<>();
      return createToken(claims, email);
    }

    /**
     * Método para la creación de un token JWT
     * @param claims
     * @param email
     * @return
     */
    public String createToken(Map<String, Object> claims, String email){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                /*
                    El token expirará en 1 minuto
                 */
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Método para obtener la clave de firma a partir de
     * la clave secreta
     * @return
     */
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Método para extraer el nombre de usuario (email) del token
     * @param token
     * @return
     */
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Método genérico para extraer cualquier claim del token
     * @param token
     * @param claimsResolver
     * @return
     * @param <T>
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Método para extraer todos los claims del token
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Método para extraer la fecha de expiración del token
     * @param token
     * @return
     */
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Método para validar si el token ha expirado
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * Método para validar el token, comprobando que el nombre de usuario
     * coincida y que no haya expirado.
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
}
