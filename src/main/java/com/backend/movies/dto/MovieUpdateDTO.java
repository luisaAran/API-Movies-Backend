package com.backend.movies.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
@Data
public class MovieUpdateDTO {
    private String title;
    @Min(0)
    @Max(9999)
    private Integer year;
    @Min(0)
    private Integer votes;
    private Double rating;
    private String description;
    private String imageUrl;
}
