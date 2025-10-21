package com.saadsabahuddin.chat_application_backend.controller;

import com.saadsabahuddin.chat_application_backend.dto.LoginDto;
import com.saadsabahuddin.chat_application_backend.dto.SuccessResponseDto;
import com.saadsabahuddin.chat_application_backend.entities.User;
import com.saadsabahuddin.chat_application_backend.service.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserServices userServices;

  @PostMapping("/register")
  public ResponseEntity<SuccessResponseDto<User>> registerUser(
    @RequestBody User user
  ) {
    User createdUser = userServices.register(user);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
        new SuccessResponseDto<User>(createdUser, "User Created Successfully")
      );
  }

  @PostMapping("/login")
  public ResponseEntity<SuccessResponseDto<String>> loginUser(
    @RequestBody LoginDto loginDto
  ) {
    User user = new User(loginDto.getUserName(), loginDto.getPassword());
    String token = userServices.verify(user);
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<String>(token, "User Logged In Successfully")
      );
  }
}
