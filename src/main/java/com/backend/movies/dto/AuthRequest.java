package com.backend.movies.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
/**
 * DTO para una petición de autenticación.
 * Debe contener el nombre de usuario y la contraseña,
 * a partir de los cuales se generará un token JWT
 */
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank
    @Email
    private String username;
    @NotBlank
    @Size(min=6)
    private String password;
}
