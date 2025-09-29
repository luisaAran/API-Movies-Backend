package com.backend.movies.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @Email
    private String email;
    @Size(min = 6)
    private String password;
    private String name;
    private String address;
    private String phoneNumber;
}
