package com.backend.movies.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(){
      super("No se encontró un recurso con las credenciales dadas");
    }
}
