package com.saadsabahuddin.chat_application_backend.exceptions;

public class UnauthenticatedException extends RuntimeException {

  public UnauthenticatedException(String message) {
    super(message);
  }
}
