package com.backend.movies.models;

import com.backend.movies.utils.ApplicationRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String email;
    private String password;
    private String address;
    private String phone_number;
    @Enumerated(EnumType.STRING)
    private ApplicationRole role; // e.g., "USER", "ADMIN"
}
