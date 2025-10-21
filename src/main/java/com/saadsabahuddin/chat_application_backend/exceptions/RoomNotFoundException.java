package com.saadsabahuddin.chat_application_backend.exceptions;

public class RoomNotFoundException extends RuntimeException {

  public RoomNotFoundException(String message) {
    super(message);
  }
}
