package com.backend.movies.exceptions;

public class InvalidScoreException extends RuntimeException{
  public InvalidScoreException(String message) {
    super(message);
  }
}
