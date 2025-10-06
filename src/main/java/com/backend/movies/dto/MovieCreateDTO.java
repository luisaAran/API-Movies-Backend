package com.backend.movies.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MovieCreateDTO {
    @NotBlank
    private String title;
    @Min(0)
    @Max(9999)
    @NotNull
    private Integer year;
    @Min(0)
    private Integer votes;
    private Double rating;
    private String description;
    private String imageUrl;
}
