package com.backend.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
}
