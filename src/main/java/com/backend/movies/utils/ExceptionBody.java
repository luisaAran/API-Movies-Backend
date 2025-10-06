package com.backend.movies.utils;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
@Builder
@Data
/**
 * Clase que representa el cuerpo de una excepción.
 * Contiene información sobre el error, el mensaje y la marca de tiempo.
 */
public class ExceptionBody{
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
