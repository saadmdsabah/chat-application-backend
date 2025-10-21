package com.saadsabahuddin.chat_application_backend.exceptions;

public class RoomExistsException extends RuntimeException {

  public RoomExistsException(String message) {
    super(message);
  }
}
