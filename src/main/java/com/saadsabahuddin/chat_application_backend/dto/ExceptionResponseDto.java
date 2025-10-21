package com.saadsabahuddin.chat_application_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponseDto {

  private String message;
  private String endPoint;
  private boolean success;
  private long timestamp;

  public ExceptionResponseDto(String errorMessage, String endPoint) {
    this.message = errorMessage;
    this.endPoint = endPoint;
    this.success = false;
    this.timestamp = System.currentTimeMillis();
  }
}
