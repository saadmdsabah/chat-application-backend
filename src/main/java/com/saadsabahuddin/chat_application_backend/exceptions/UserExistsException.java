package com.saadsabahuddin.chat_application_backend.exceptions;

public class UserExistsException extends RuntimeException {

  public UserExistsException(String message) {
    super(message);
  }
}
