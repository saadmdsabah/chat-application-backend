package com.saadsabahuddin.chat_application_backend.controller;

import com.saadsabahuddin.chat_application_backend.dto.ExceptionResponseDto;
import com.saadsabahuddin.chat_application_backend.exceptions.ResourceNotFoundException;
import com.saadsabahuddin.chat_application_backend.exceptions.RoomExistsException;
import com.saadsabahuddin.chat_application_backend.exceptions.RoomNotFoundException;
import com.saadsabahuddin.chat_application_backend.exceptions.UnauthenticatedException;
import com.saadsabahuddin.chat_application_backend.exceptions.UserExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponseDto> handleException(
    Exception ex,
    WebRequest request
  ) {
    ExceptionResponseDto response = new ExceptionResponseDto(
      ex.getMessage(),
      request.getDescription(false)
    );
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ExceptionResponseDto> handleResourceNotFound(
    ResourceNotFoundException ex,
    WebRequest request
  ) {
    ExceptionResponseDto response = new ExceptionResponseDto(
      ex.getMessage(),
      request.getDescription(false)
    );
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UnauthenticatedException.class)
  public ResponseEntity<ExceptionResponseDto> handleUnauthenticatedException(
    UnauthenticatedException ex,
    WebRequest request
  ) {
    ExceptionResponseDto response = new ExceptionResponseDto(
      ex.getMessage(),
      request.getDescription(false)
    );
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ExceptionResponseDto> handleUsernameNotFoundException(
    UsernameNotFoundException ex,
    WebRequest request
  ) {
    ExceptionResponseDto response = new ExceptionResponseDto(
      ex.getMessage(),
      request.getDescription(false)
    );
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(RoomNotFoundException.class)
  public ResponseEntity<ExceptionResponseDto> handleRoomNotFoundException(
    RoomNotFoundException ex,
    WebRequest request
  ) {
    ExceptionResponseDto response = new ExceptionResponseDto(
      ex.getMessage(),
      request.getDescription(false)
    );
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserExistsException.class)
  public ResponseEntity<ExceptionResponseDto> handleUserExistsException(
    UserExistsException ex,
    WebRequest request
  ) {
    ExceptionResponseDto response = new ExceptionResponseDto(
      ex.getMessage(),
      request.getDescription(false)
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RoomExistsException.class)
  public ResponseEntity<ExceptionResponseDto> handleRoomExistsException(
    RoomExistsException ex,
    WebRequest request
  ) {
    ExceptionResponseDto response = new ExceptionResponseDto(
      ex.getMessage(),
      request.getDescription(false)
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
