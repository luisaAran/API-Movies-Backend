package com.backend.movies.models;

import com.backend.movies.utils.ApplicationRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: luisaAran
 * Entidad que representa la información del usuario en
 * la base de datos.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ToString
@Table(name = "users")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Email
    private String email;
    @Size(min = 6)
    private String password;
    private String address;
    private String phone_number;
    @Enumerated(EnumType.STRING)
    private ApplicationRole role; // e.g., "USER", "ADMIN"
}
