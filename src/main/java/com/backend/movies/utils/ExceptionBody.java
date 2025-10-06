package com.backend.movies.utils;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
@Builder
@Data
public class ExceptionBody{
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
