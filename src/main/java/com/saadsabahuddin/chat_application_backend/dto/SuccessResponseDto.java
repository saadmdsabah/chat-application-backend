package com.saadsabahuddin.chat_application_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponseDto<T> {

  private boolean success;
  private String message;
  private T data;
  private long timestamp;

  public SuccessResponseDto(T data, String message) {
    this.success = true;
    this.message = message;
    this.data = data;
    this.timestamp = System.currentTimeMillis();
  }
}
